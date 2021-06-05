package com.example.socialapp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.Post;
import com.example.socialapp.main.ui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Todo o trabalho de comecar e lidar com a conexao bluetooth com outros dispositivos e feito aqui.
 * Usa 3 threads, uma que escuta, uma que conecta e uma que lida com a conexao quando essa for
 * estabelecida.
 */
public class BluetoothChatService {

    AppDatabase database;


    private static final String NAME_SECURE = "BluetoothChatSecure";

    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mSecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private int mNewState;

    // Constantes para cada estado da conexao
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    /**
     * Construtor para a sessao de chat bluetooth
     *
     * @param context A view atual
     * @param handler handler para mandar de volta ao context
     */
    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
        mHandler = handler;
        database = AppDatabase.getDatabase(context);
    }

    /**
     * Trocar a UI de acordo com o estado da conexao
     */
    private synchronized void updateUserInterfaceTitle() {
        mState = getState();
        Log.d("BluetoothChatService", "updateUserInterfaceTitle() " + mNewState + " -> " + mState);
        mNewState = mState;

        // Dar o novo estado ao handler
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }

    /**
     * Retorna o estado atual (daqueles 4) da conexao
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Comeca o servico de chat, comecando uma AcceptThread e ouvindo em modo de server.
     * E chamado pelo onResume()
     */
    public synchronized void start() {
        Log.d("BluetoothChatService", "start");

        // Cancelar threads tentando fazer a conexao
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancelar threads que estao conectadas
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Comecar a thread para ouvir
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }
        // atualizar a UI
        updateUserInterfaceTitle();
    }

    /**
     * Come a ConnectThread para conectar a um dispositivo
     *
     * @param device O dispositivo
     */
    public synchronized void connect(BluetoothDevice device) {
        Log.d("BluetoothChatService", "connect to: " + device);

        // Cancelar threads tentando fazer a conexao
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancelar threads que estao conectadas
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Comecar a thread para ouvir
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        // Atualizar a UI
        updateUserInterfaceTitle();
    }

    /**
     * Comeca a ConnectedThread para lidar com a conexao bluetooth atual
     *
     * @param socket A BluetoothSocket da conexao
     * @param device O dispositivo a conectar
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        Log.d("BluetoothChatService", "connected, Socket Type:" + socketType);

        // Cancelar a ConnectThread ja que ja estamos connectados
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancelar qualquer outra conexao actual
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancelar a acceptthread ja que so queremos nos ligar a 1 dispositivo
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        // Comecar a thread e comecar a transmitir
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Mandar o nome do dispositivo de volta a atividade da UI
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);


        // atualizar a UI
        updateUserInterfaceTitle();
    }

    /**
     * Parar TODAS as threads
     */
    public synchronized void stop() {
        Log.d("BluetoothChatService", "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        mState = STATE_NONE;
        // Atualizar a UI
        updateUserInterfaceTitle();
    }

    /**
     * Escrever para a ConnectThread
     * De forma asincrona
     *
     * @param out O que escrever (array de bytes)
     */
    public void write(byte[] out) {
        ConnectedThread r;
        // sincronizar uma copia da connectedthread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // escrever de forma asincrona
        r.write(out);
    }

    /**
     * Indicar que a conexao falhou e informar a UI
     */
    private void connectionFailed() {
        // Mandar a mensagem de falha de volta
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // atualizar o titulo da UI
        updateUserInterfaceTitle();

        // Comecar o servico mais uma vez
        BluetoothChatService.this.start();
    }

    /**
     * Indicar que a conexao foi perdida e informar o utilizador
     */
    private void connectionLost() {
        // Mandar a mensagem de falha de volta a UI
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // atualizar o titulo da UI
        updateUserInterfaceTitle();

        // Comecar o servico mais uma vez
        BluetoothChatService.this.start();
    }

    /**
     * Esse thread executa enquanto escuta a conexoes.
     * Ela funciona como um client server-side, executa ate a conexao for aceite ou cancelada
     */
    private class AcceptThread extends Thread {
        // A server socket local
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Criar novo server socket para ouvir
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
            } catch (IOException e) {
                Log.e("BluetoothChatService", "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }

        public void run() {
            Log.d("BluetoothChatService", "Socket Type: " + mSocketType +
                    "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket;

            //  Ourvir a server socket para ver se estamos conectados
            while (mState != STATE_CONNECTED) {
                try {
                    // Esse call so retorna se a conexao for aceite ou haver exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e("BluetoothChatService", "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // Se for aceite...
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Tudo bom, comecar a connectrhread
                                connected(socket, socket.getRemoteDevice(),
                                        mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Nao esta pronto ou nao conectou, fechar a socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e("BluetoothChatService", "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i("BluetoothChatService", "END mAcceptThread, socket Type: " + mSocketType);

        }

        public void cancel() {
            Log.d("BluetoothChatService", "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("BluetoothChatService", "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }


    /**
     * Essa thread executa enquanto tenta fazer uma conexao a um dispositivo.
     * Executa ate sucedir ou falhar
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = "Secure";

            // pegar a BluetoothSocket usando o BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            } catch (IOException e) {
                Log.e("BluetoothChatService", "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            Log.i("BluetoothChatService", "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Lembrar de cancelar a descoberta depois disso, para nao deixar tudo lerdo
            mAdapter.cancelDiscovery();

            // conectar a BluetoothSocket
            try {
                //So retorna quando for sucedido ou em excessao
                mmSocket.connect();
            } catch (IOException e) {
                // fechar a socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e("BluetoothChatService", "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Acabar com a ConnectThread quando acabamos
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }

            // Comecar a ConnectedThread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BluetoothChatService", "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * Essa thread roda enquanto ha a conexao com um dispositivo remoto.
     * Lida com todas as conexoes entrando e saindo
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d("BluetoothChatService", "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Pegar streams de input e output de BluetoothSocket
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("BluetoothChatService", "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            Log.i("BluetoothChatService", "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

          
            List<Post> allposts = database.getDao().getAllPosts();

            Message msg2;

            for (Post p : allposts) {
                Log.d("BluetoothChatService", "Got in here bro");
                Log.d("BluetoothChatService", "autor: " + p.getPost_author());
                Log.d("BluetoothChatService", "Mensagem: " + p.getMessage());

             

                String inputString = "@ " + p.getPostID() + " " + p.getPost_author().trim() + " " + p.getMessage();
                byte[] byteArrray = inputString.getBytes();
                write(byteArrray);
            }



      

         

            // Continuar ouvindo a InputStream enquanto conectada
            while (mState == STATE_CONNECTED) {
                try {
                    // Ler da InputStream
                    bytes = mmInStream.read(buffer);

                    // Mandar os bytes obtidos a atividade UI
                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    Log.e("BluetoothChatService", "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Escrever para a OutStream conectada
         *
         * @param buffer O que escrever (array de bytes)
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Mandar a mensagem de volta a atividade UI
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                Log.e("BluetoothChatService", "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BluetoothChatService", "close() of connect socket failed", e);
            }
        }
    }
}
