

package com.example.socialapp.bluetooth;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.socialapp.R;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.Post;
import com.example.socialapp.recyclerview.Feed;


/**
 * Fragmento criado por BluetoothChatHub. Controla o chat e as mensagens
 */
public class BluetoothChatFragment extends Fragment {

    // Codigos das acoes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    AppDatabase database;

    // Layout
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    private String mConnectedDeviceName = null;


    private ArrayAdapter<String> mConversationArrayAdapter; //array para guardar as mensagens do chat


    private StringBuffer mOutStringBuffer; //buffer para o que sai


    private BluetoothAdapter mBluetoothAdapter = null;

    private BluetoothChatService mChatService = null; //objeto pros servicos de chat

    @Override
    public void onCreate(Bundle savedInstanceState) {
        database = AppDatabase.getDatabase(getContext());
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Pegar o adaptador bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Se nao existe, nao ha bluetooth
        FragmentActivity activity = getActivity();
        if (mBluetoothAdapter == null && activity != null) {
            Toast.makeText(activity, "Bluetooth não esta disponível", Toast.LENGTH_LONG).show();
            activity.finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBluetoothAdapter == null) {
            return;
        }
        // Se o bluetooth estiver off, pedir pra ligar
        // e entao setupChat() e chamado durante onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            //parar o bluetooth caso a atividade for desligada
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //caso em que o bluetooth foi ligado depois da app for ligada, enquanto ela estava pausada
        //onResume e chamado quando ACTION_REQUEST_ENABLE retorna

        if (mChatService != null) {
            // so nao comecamos se o estado for NONE
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflar os menus...
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = view.findViewById(R.id.in);
        mOutEditText = view.findViewById(R.id.edit_text_out);
        mSendButton = view.findViewById(R.id.button_send);
    }

    /**
     * Preparar UI e atividades de background pro bluetooth
     */
    private void setupChat() {
        Log.d("BluetoothChatFragment", "setupChat()");

        // Initialize the array adapter for the conversation thread
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        mConversationArrayAdapter = new ArrayAdapter<>(activity, R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        //Inicializer o arraydapater para mandar
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // inicializar o send button
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // mandar uma mensagem com o que esta dentro do campo de mensagem
                View view = getView();
                if (null != view) {
                    TextView textView = view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });

        // inicializar o servico bluetooth
        mChatService = new BluetoothChatService(activity, mHandler);

        // buffer para mensagens saindo
        mOutStringBuffer = new StringBuffer();
    }

    /**
     * Ligar o bluetooth por 5 minutos
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Mandar a mensagem
     *
     * @param message String com o texto a ser mandado.
     */
    private void sendMessage(String message) {
        // Ver se estamos ligados a algo
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Ver se ha texto a ser mandado
        if (message.length() > 0) {
            // transformar a mensagem em um array de bytes para mandar
            byte[] send = message.getBytes();
            mChatService.write(send);

            // limpar o chat e o buffer apos
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * listener para o EditText controlando a mensagem a ser mandada
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // Se a acao for um enter, mandar a mensagem.
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

     /**
      * Mudar o status da pagina se estamos conectados
      *
      * @param resId int do ID do estado
      */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Mesmo que acima, mas com uma mensagem como argumento ao inves
     *
     * @param subTitle status desejado
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * Hander que pega a info de volta de BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            if (msg.what == Constants.MESSAGE_STATE_CHANGE) {
                switch (msg.arg1) {
                    case BluetoothChatService.STATE_CONNECTED:
                        // setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                        setStatus("connected to something"); //change to upper...
                        mConversationArrayAdapter.clear();
                        break;
                    case BluetoothChatService.STATE_CONNECTING:
                        setStatus(R.string.title_connecting);
                        break;
                    case BluetoothChatService.STATE_LISTEN:
                    case BluetoothChatService.STATE_NONE:
                        setStatus(R.string.title_not_connected);
                        break;
                }
            } else if (msg.what == Constants.MESSAGE_WRITE) {
                byte[] writeBuf = (byte[]) msg.obj;
                // construir string do buffer para adicionar ao arrayadapter
                String writeMessage = new String(writeBuf);
                if(writeMessage.length() != 0){
                    if(writeMessage.charAt(0) == '@'){
                        //don't wanna send this message to ourselves
                    }else{
                        mConversationArrayAdapter.add("Me:  " + writeMessage);
                    }
                }

            } else if (msg.what == Constants.MESSAGE_READ) {
                byte[] readBuf = (byte[]) msg.obj;
                // construir string do buffer recebido
                String readMessage = new String(readBuf, 0, msg.arg1);
                if(readMessage.length() != 0){
                    if(readMessage.charAt(0) == '@'){
                        //it's a post horray
                        Log.d("BluetoothChatFragment", "You received a post :) POST: " + readMessage);

                        String[] parts = readMessage.split(" ");
                        if(!parts[3].contains("@")){
                            Post p = new Post();
                            Log.d("BluetoothChatFragment", "Post ID: " + parts[1]);
                            Log.d("BluetoothChatFragment", "Autor " + parts[2]);
                            Log.d("BluetoothChatFragment", "Message: " + parts[3]);
                            p.setPostID(parts[1]);
                            p.setPost_author(parts[2]);
                            p.setMessage(parts[3]);

                            database.getDao().insertPost(p);
                        }
                    }else if(readMessage.charAt(0) == '#'){
                        String[] parts = readMessage.split(" ", 2);
                        mConnectedDeviceName = parts[1];
                    }else{
                        mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    }
                }
            } else if (msg.what == Constants.MESSAGE_DEVICE_NAME) {// save the connected device's name
                mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                if (null != activity) {
                    Toast.makeText(activity, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == Constants.MESSAGE_TOAST) {
                if (null != activity) {
                    Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                }
            } else if(msg.what == Constants.MESSAGE_POST){
                //do stuff :)
                /*
                String elPost = msg.getData().getString(Constants.DEVICE_NAME);
                Log.d("BluetoothChatFragment", "POST: " + elPost);

                String[] stuff = elPost.split(" ", 2);
                //eu estav aaqui :)

                Log.d("BluetoothChatFragment", "POST: " + stuff[0]);
                Log.d("BluetoothChatFragment", "POST: " + stuff[1]);

                Post p = new Post();
                p.setPost_author(stuff[0]);
                p.setMessage(stuff[1]);
                database.getDao().insertPost(p);

                */
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // quando DeviceListActivity retorna com o dispositivo a conectar
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                // quando o pedido para ligar o bluetooth retorna
                if (resultCode == Activity.RESULT_OK) {
                    // bluetooth ligado, pode dar setup no chat
                    setupChat();
                } else {
                    // bluetooth nao foi ligado (user nao habilitou)
                    Log.d("BluetoothChatFragment", "BT not enabled");
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        Toast.makeText(activity, R.string.bt_not_enabled_leaving,
                                Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                }
        }
    }

    /**
     * Fazer a conexao com outro dispositivo
     *
     * @param data    {@link Intent} com  {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     */
    private void connectDevice(Intent data) {
        // Pegar o mac adress do dispotivo
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        String address = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // pegar o objeto BluetoothDevice
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // conectar
        mChatService.connect(device);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        //inflar o menu
        inflater.inflate(R.menu.bluetooth_chat, menu);
        Log.v("BluetoothChatFragment", "I'm inflating");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.secure_connect_scan) {// iniciar devicelist e scanear por disps pairados
            Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        } else if (itemId == R.id.discoverable) {// ver se o dispositivo pode ser descoberto
            ensureDiscoverable();
            return true;
        }
        return false;
    }

}
