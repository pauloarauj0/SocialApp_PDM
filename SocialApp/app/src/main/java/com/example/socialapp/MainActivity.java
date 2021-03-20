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
    private EditText username;
    private EditText senha;

    //usuarios regitados
    static User [] users = new User[100];
    static User UserAtual = new User();
    boolean DEBUG = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        senha = findViewById(R.id.senha);
        Button login = findViewById(R.id.login);
        TextView create_login = findViewById(R.id.main_create);

        //clicar no botao login e iniciar sessao
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_username = username.getText().toString();
                String input_senha =  senha.getText().toString();


                if(DEBUG){
                    debugmode();
                }
                if(input_username.isEmpty() || input_senha.isEmpty()){
                    Toast.makeText(MainActivity.this, "Um dos campos não foi preenchido", Toast.LENGTH_SHORT).show();
                }else{
                    if(isValid(input_username,input_senha)){
                        Toast.makeText(MainActivity.this, "Bem vindo! " + input_username, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                }

                //debug



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
     * Debug mode: Sem necessidade de colocar user nem password
     */
    private boolean debugmode() {
        if(DEBUG){

            return true;
        }
        return false;
    }

    /**
     *  verificar se o username e a password estao corretos
     * @param input_username  Username colocada pelo usuario (string)
     * @param input_senha     Senha colocada pelo usuario (string)
     * @return boolean True caso seja um user registado e com as credenciais corretas
     */
    private boolean isValid( String input_username, String input_senha) {

        for(int i=0; i< User.nUser; i++){
            if(users[i]==null){
                Log.i("MAIN NULL", "Null\n " );
                return false;
            }
            if(users[i].username.equals(input_username) && users[i].password.equals(input_senha)){
                UserAtual.username = users[i].username;
                UserAtual.password = users[i].password;
                UserAtual.email = users[i].email;


                return true;
            }

        }

        return false;
    }
}