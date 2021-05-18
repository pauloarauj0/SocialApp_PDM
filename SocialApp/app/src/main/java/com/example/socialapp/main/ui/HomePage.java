package com.example.socialapp.main.ui;

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

import com.example.socialapp.R;
import com.example.socialapp.bluetooth.BluetoothChatHub;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.recyclerview.Feed;

public class HomePage extends AppCompatActivity {

    private Button chat;
    private Button info;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        chat = findViewById(R.id.home_chat);
        info = findViewById(R.id.home_info);

        //info
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage.this, "User: "+MainActivity.AccAtual.getUserName()  +"\n"+ "Email: "+ MainActivity.AccAtual.getEmail()+"\n"+
                        "Senha: "+MainActivity.AccAtual.getUserPass() +"\n"+ "Data: "+MainActivity.AccAtual.getUserAniversario()+"\n"
                        +"Descricao: "+ MainActivity.AccAtual.getUserDesc(), Toast.LENGTH_SHORT).show();
            }
        });


        //entrar no chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("HOMEPAGE ", "Clicou em Chat");

                Intent intent = new Intent(HomePage.this, BluetoothChatHub.class);
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
            case R.id.feed:
                Intent intent4 = new Intent(HomePage.this, Feed.class);
                startActivity(intent4);
                return true;
            default:
                return true;
        }
    }
}

