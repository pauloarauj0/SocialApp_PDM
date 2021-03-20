package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class DeviceList extends AppCompatActivity {

    private ListView listAvailableDevices, listPairedDevices;
    private ArrayAdapter<String> adapterAvailableDevices, adapterPairedDevices;
    private BluetoothAdapter bluetoothAdapter;
    private ProgressBar progressScanDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        listAvailableDevices = findViewById(R.id.list_available_devices);
        listPairedDevices = findViewById(R.id.list_paired_devices);
        progressScanDevices = findViewById(R.id.progress_scan_devices);



        adapterAvailableDevices = new ArrayAdapter<String>(this, R.layout.device_list_item);
        adapterPairedDevices = new ArrayAdapter<String>(this, R.layout.device_list_item);

        if(adapterAvailableDevices != null) listAvailableDevices.setAdapter(adapterAvailableDevices);
        if(adapterPairedDevices != null) listPairedDevices.setAdapter(adapterPairedDevices);

        listAvailableDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString(); //address + name
                String address = info.substring(info.length() - 17); // subtract 17 -> just the address

                Intent intent = new Intent();
                intent.putExtra("deviceAddress", address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices != null && pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                adapterPairedDevices.add(device.getName() + " \n" + device.getAddress());
            }
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDeviceListener, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothDeviceListener, intentFilter1);
    }

    private BroadcastReceiver bluetoothDeviceListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    adapterAvailableDevices.add(device.getName() + "\n" + device.getAddress());
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressScanDevices.setVisibility(View.GONE);
                if (adapterAvailableDevices.getCount() == 0) {
                    Toast.makeText(context, "No new devices found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Click on the device to start the chat", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetooth_menu_available, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_scan_devices:
                Toast.makeText(this, "Scan devices has been clicked", Toast.LENGTH_SHORT).show();
                scanDevices();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void scanDevices(){
        progressScanDevices.setVisibility(View.VISIBLE);
        adapterAvailableDevices.clear();

        Toast.makeText(this, "Scanning for devices", Toast.LENGTH_SHORT).show();

        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();
    }
}