package com.example.socialapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn, loginBut, regBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        //bluetooth
        mStatusBlueTv = findViewById(R.id.statusBluetoothIv);
        mPairedTv     = findViewById(R.id.pairedTv);
        mBlueIv       = findViewById(R.id.bluetoothIv);
        mOnBtn        = findViewById(R.id.onBtn);
        mOffBtn       = findViewById(R.id.offBtn);
        mDiscoverBtn  = findViewById(R.id.discoverableBtn);
        mPairedBtn    = findViewById(R.id.pairedBtn);

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();


        //check if bluetooth is available
        if (mBlueAdapter == null){
           mStatusBlueTv.setText("Bluetooth is not available");

        }else{
            mStatusBlueTv.setText("Bluetooth is available");
        }

        //set image according to bluetooth status
        /*
        if(mBlueAdapter.isEnabled()){
            mBlueIv.setImageResource(R.drawable.ic_action_name);
        }else{
            mBlueIv.setImageResource(R.drawable.ic_action_off);
        }
        */

        if(mOnBtn == null){
            Log.i("Bluetooth", "Null");

        }
        else
        //on btn click
        mOnBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mBlueAdapter.isEnabled()){
                    showToast("Turning on Bluetooth");
                    //intent to bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }else{
                    showToast("Bluetooth is already on");
                }
            }
        });

        //discover bluetooth btn

        mDiscoverBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mBlueAdapter.isDiscovering()){
                    showToast("Making your device discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        //off btn

        mOffBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable();
                    showToast("Turning off bluetooth");
                  //mBlueIv.setImageResource(R.drawable.ic_action_off);
                }else{
                    showToast("Bluetooth is off already");
                }
            }
        });

        //get paired device

        mPairedBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mBlueAdapter.isEnabled()){
                    mPairedTv.setText("Paired Devices:");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for(BluetoothDevice device: devices){
                        mPairedTv.append("\nDevice: " + device.getName() + "," + device);
                    }
                }else{
                    //bluetooth is off so no paired devices
                    showToast("Turn on bluetooth to use this feature");
                }
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    //bluetooth is on
                   // mBlueIv.setImageResource(R.drawable.ic_action_name);
                    showToast("Bluetooth is on");
                }else{
                    //user denied to enable bluetooth
                    showToast("Could not turn on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //toast message function
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}