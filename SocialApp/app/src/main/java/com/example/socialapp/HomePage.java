package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    private TextView welcomeText;
    private Button settings;
    private Button chat;
    private ImageView bluetoothBtn;
    private Button info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);





        //FAZER: colocar nome do user a seguir a bem vindo
        welcomeText = findViewById(R.id.home_wc);
        chat = findViewById(R.id.home_chat);
        bluetoothBtn = findViewById(R.id.bluetooth_icon);
        info = findViewById(R.id.home_info);

        //info
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage.this, "User: "+MainActivity.UserAtual.username  +"\n"+ "Email: "+ MainActivity.UserAtual.email+"\n"+
                        "Senha: "+MainActivity.UserAtual.password +"\n"+ "Data: "+MainActivity.UserAtual.aniversario+"\n"
                        +"Descricao: "+ MainActivity.UserAtual.descricao, Toast.LENGTH_SHORT).show();
            }
        });


        //entrar no chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("HOMEPAGE ", "Clicou em Enter");

                Intent intent = new Intent(HomePage.this, ChatAndFeed.class);
                startActivity(intent);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(HomePage.this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                Intent intent2 = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.change_password:
                Intent intent3 = new Intent(HomePage.this, ChangePassword.class);
                startActivity(intent3);
                return true;
            default:
                return true;
        }
    }
}

