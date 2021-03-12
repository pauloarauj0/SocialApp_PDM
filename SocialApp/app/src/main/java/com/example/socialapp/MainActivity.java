package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText senha;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        senha = findViewById(R.id.senha);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_username = username.getText().toString();
                String input_senha =  senha.getText().toString();


                if(input_username.isEmpty() || input_senha.isEmpty()){
                    Toast.makeText(MainActivity.this, "Um dos campos não foi preenchido", Toast.LENGTH_SHORT).show();
                }else{
                    if(isValid(input_username,input_senha)){
                        Toast.makeText(MainActivity.this, "Bem vindo! " + input_username, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, CriarLogin.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private boolean isValid(String input_username, String input_senha) {
        if(input_username.equals("admin") && input_senha.equals("123"))
            return true;
        return false;
    }
}