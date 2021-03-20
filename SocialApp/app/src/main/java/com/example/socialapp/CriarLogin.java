package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CriarLogin extends AppCompatActivity {
    private EditText loginField, passField, confirmField,emailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_login);

        Button create_login = findViewById(R.id.reg_create);
        loginField = findViewById(R.id.reg_name);
        passField = findViewById(R.id.reg_password);
        confirmField = findViewById(R.id.reg_confirm);
        emailField = findViewById(R.id.reg_email);

        create_login.setOnClickListener(new View.OnClickListener() {

            //submeter o login
            @Override
            public void onClick(View v) {
                String name = loginField.getText().toString();
                String pass = passField.getText().toString();
                String confirm_pass = confirmField.getText().toString();
                String email = emailField.getText().toString();


                if(name.isEmpty() || pass.isEmpty() || confirm_pass.isEmpty() || email.isEmpty()){
                    Toast.makeText(CriarLogin.this, "Um dos campos não foi preenchido", Toast.LENGTH_SHORT).show();
                    Log.v("CRIARLOGIN: ", "Parametros vazios" );

                }
                else if(!(pass.equals(confirm_pass))){
                    Toast.makeText(CriarLogin.this, "As senhas não coincidem ", Toast.LENGTH_SHORT).show();
                    Log.v("CRIARLOGIN: ", "senhas diferentes" + "Senha1: "+pass + " Senha2 "+ confirm_pass);

                }
                else{
                    createAccount(name,pass,email);

                    Toast.makeText(CriarLogin.this, "Login criado com sucesso ", Toast.LENGTH_SHORT).show();
                    Log.v("CRIARLOGIN: ", "Conta criada :\n " );

                    finish();
                }
            }
        });

    }

    /**
     * Criar uma nova conta
     *
     * @param name String
     * @param pass String
     * @param email String
     */
    private void createAccount(String name, String pass,String email) {
        User user = new User(name,pass,email);
        MainActivity.users[User.nUser-1] = user;
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}