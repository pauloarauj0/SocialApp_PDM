package com.example.socialapp.main.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialapp.R;
import com.example.socialapp.data.base.Account;
import com.example.socialapp.data.base.AppDatabase;
import com.google.android.material.appbar.AppBarLayout;

public class CriarLogin extends AppCompatActivity {
    private EditText loginField, passField, confirmField,emailField;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_login);
        database = AppDatabase.getDatabase(CriarLogin.this);

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
                    Account account = new Account();
                    account.setEmail(email);
                    account.setUserName(name);
                    account.setUserPass(pass);

                    long inseriu = database.getDao().insertAccount(account);
                    if(inseriu!=-1){

                        Toast.makeText(CriarLogin.this, "Login criado com sucesso ", Toast.LENGTH_SHORT).show();
                        Log.v("CRIARLOGIN: ", "Conta criada :\n " );
                        finish();
                    }
                    else{
                        Toast.makeText(CriarLogin.this, "Email em uso ", Toast.LENGTH_SHORT).show();
                        Log.v("CRIARLOGIN: ", "Conta invalida:\n " );
                    }




                }
            }
        });

    }



    @Override
    public void onBackPressed(){
        finish();
    }
}