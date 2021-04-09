package com.example.socialapp.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.socialapp.R;

import java.util.Set;

/**
 * A atividade para lancar a lista com todos os dispositivos numa nova janela.
 * Ela lista todos os dispositivos pairados no momento e os dispositivos detectados apos a descoberta
 * Quando um dispositivo for escolhido pelo utilizador, seu MAC address e mandado de volta ao pai
 * a Activity esta no Intent resultado
 */
public class DeviceListActivity extends Activity {


    /**
     * O extra para o retorno da intent
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter mBtAdapter;

    /**
     * Dispositivos novos conectados
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Preparar a janela
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);

        // Mandar o resultado como cancelado se o usuario sair
        setResult(Activity.RESULT_CANCELED);

        // Comecar o botao e comecar a descoberta
        Button scanButton = findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        //Comecar novos array adapaters
        //um para dispositivos pairados e um para conectados
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        // Botar os pairados numa ListView
        ListView pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Botar os achados numa ListView
        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Registrar para broadcast quando descoberto
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Registrar para broadcast quando a descoberta acabar
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Pegar o BluetoothAdapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Pegar todos os dispositivos pairados
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Se houver dispositivos pairados, adiciona-los ao ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Certificar que nao estamos descobrindo ainda quando a atividade for fechada
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Tirar todos os broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Comecar descoberta de dispositivo com o BluetoothAdapter
     */
    private void doDiscovery() {
        Log.d( "DeviceListActivity", "doDiscovery()");

        // Indicar no titulo que se esta escaneando
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Ligar a legenda para novos dispositivos tambem
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // Parar de descobrir, se ja estamos
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Pedir a descoberta ao BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    /**
     * O que fazer quando o usuario clica em qualquer dispositivo das ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            //Cancelar a descoberta quando estamos conectados
            mBtAdapter.cancelDiscovery();

            //Pegar o mac adress (ultimos 17 caracteres da view)
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Criar o intent de resultado com esse mac adress
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Botar o resultado como OK e acabar a atividade
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    /**
     * O BroadcastReceiver que ouve para dispositivos descobertos e muda o titulo quando essa
     * descoberta acabar
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Quando um dispositivo e achado...
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Betar o BluetoothDevice do intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Pular a parte de lista-lo se ja esta pairado, caso contrario seria listado 2 vezes
                if (device != null && device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // quando a descoberta acabar, mudar o titulo
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
