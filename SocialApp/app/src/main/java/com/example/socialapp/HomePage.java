package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    private TextView welcomeText;
    private Button settings;
    private Button chat;
    private ImageView bluetoothBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //FAZER: colocar nome do user a seguir a bem vindo
        welcomeText = findViewById(R.id.home_wc);
        settings = findViewById(R.id.home_settings);
        chat = findViewById(R.id.home_chat);
        bluetoothBtn = findViewById(R.id.bluetooth_icon);

        //entrar no chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("HOMEPAGE ", "Clicou em Enter");

                Intent intent = new Intent(HomePage.this, ChatAndFeed.class);
                startActivity(intent);
            }
        });

        //modificar perfil
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("HOMEPAGE", "Clicou em Settings");

                Intent intent = new Intent(HomePage.this, Settings.class);
                startActivity(intent);
                //finish();

            }
        });

        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HOMEPAGE", "Clicou no bluetooth");

                Intent intent = new Intent(HomePage.this, Bluetooth_chat.class);
                startActivity(intent);

            }
        });

    }
}