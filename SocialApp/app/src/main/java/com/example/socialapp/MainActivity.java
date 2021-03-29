package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int INVALID = -1;
    private EditText email;
    private EditText senha;

    //usuarios regitados
    static User [] users = new User[100];
    static User UserAtual = new User("user","atual");

    private boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
     * Confima o login e define o user atual
     * @param input_email Email do user
     * @param input_senha Senha do user
     * @return True caso confirme o login
     */
    private boolean confirmLogin(String input_email, String input_senha) {
        User login = new User(input_email,input_senha);
        int pos = login.findMatch();

        if(pos!=INVALID){
            setUserAtual(pos);
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