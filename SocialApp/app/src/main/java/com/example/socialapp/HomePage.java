package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    private TextView welcomeText;
    private Button settings;
    private Button chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //FAZER: colocar nome do user a seguir a bem vindo
        welcomeText = findViewById(R.id.home_wc);
        settings = findViewById(R.id.home_settings);
        chat = findViewById(R.id.home_chat);

        //entrar no chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //modificar perfil
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("INFO: ", "Clicou em Settings");

            }
        });

    }
}