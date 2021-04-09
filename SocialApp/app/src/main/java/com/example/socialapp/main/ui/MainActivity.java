package com.example.socialapp.main.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.R;
import com.example.socialapp.data.base.Account;
import com.example.socialapp.data.base.AppDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int INVALID = -1;
    private EditText email;
    private EditText senha;
    List<Account> accounts = new ArrayList<>();
    AppDatabase database;


    //usuarios regitados
    static User [] users = new User[100];
    static User UserAtual = new User("user","atual");

    private boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = AppDatabase.getDatabase(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database.getDao().deleteDatabase();

        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        Button login = findViewById(R.id.login);
        TextView create_login = findViewById(R.id.main_create);

        //clicar no botao login e iniciar sessao
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_email = email.getText().toString();
                String input_senha =  senha.getText().toString();


                if(DEBUG){
                    debugMode();
                }
                else{
                    if(input_email.isEmpty() || input_senha.isEmpty()){
                        Toast.makeText(MainActivity.this, "Um dos campos não foi preenchido", Toast.LENGTH_SHORT).show();
                    }else{
                        if(confirmLogin(input_email,input_senha)){
                            Toast.makeText(MainActivity.this, "Bem vindo! " + input_email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

            }
        });

    //ir para a pagina de criar login
        create_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CriarLogin.class);
                startActivity(intent);

            }
        });
    }

    /**
     * Saltar a criaçao de conta
     */
    private void debugMode() {
        Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);
    }

    /**
     * Confima o login
     * @param input_email Email do user
     * @param input_senha Senha do user
     * @return True caso confirme o login
     */

    private boolean confirmLogin(String input_email, String input_senha){
        accounts = database.getDao().getAllAccount();
        for(Account a : accounts){
            Log.i("MAIN", "EMAIL: "+a.getEmail()+ " Pass: "+a.getUserPass());
        }
        //Log.i("MAIN", "database: "+database.getDao().findAccount(input_email, input_senha));
        int encontrou = database.getDao().findAccount(input_email, input_senha);
        if(encontrou > 0 ){
                Log.i("MAIN", "Login com sucesso");
                return true;
        }
        Log.i("MAIN", "Login Falhado");
        return false;

    }

    /**
     * Define quem é o user atual da sessao
     * @param pos Pos do user no array users
     */

    private void setUserAtual(int pos) {
            UserAtual = users[pos];

    }


}