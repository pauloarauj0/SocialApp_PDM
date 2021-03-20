package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private EditText pass, npass, npassConf;
    private Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pass = (EditText) findViewById(R.id.oldPass);
        npass = (EditText) findViewById(R.id.newPass);
        npassConf = (EditText) findViewById(R.id.newPassConf);
        confirmBtn = (Button) findViewById(R.id.confirmChangePass);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHANGEPASSWORD", "Clicked on confirm");

                String passStr, npassStr, npassConfStr;
                passStr = pass.getText().toString();
                npassStr = npass.getText().toString();
                npassConfStr = npassConf.getText().toString();

                //verificar parametros vazios
                if(passStr.isEmpty() || npassStr.isEmpty() || npassConfStr.isEmpty() ){
                    Toast.makeText(ChangePassword.this, "Um dos campos não foi preenchido", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "Parametros vazios" );
                }
                //verificar se a senha antiga coincide
                else if( !(passStr.equals(MainActivity.UserAtual.password))){
                    Toast.makeText(ChangePassword.this, "Password antiga incorreta ", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "Senha antiga incorreta");

                }
                //verificar se a nova senha coincide
                else if(!(npassStr.equals(npassConfStr))){
                    Toast.makeText(ChangePassword.this, "As senhas novas não coincidem ", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "senhas diferentes" + "Senha1: "+passStr + " Senha2 "+ npassStr);

                }
                //trocar senhas
                else{
                    Toast.makeText(ChangePassword.this, "Pass trocado com sucesso ", Toast.LENGTH_SHORT).show();
                    changePassword(npassStr);
                    finish();

                }
            }
        });
    }

    /**
     * Procura o user atual no array de usuarios, troca a senha do correto e atualiza-o no array
     * @param npassStr  Nova password
     *
     */
    private void changePassword(String npassStr) {
        for(int i=0;i<User.nUser;i++){
            if(MainActivity.users[i].username.equals(MainActivity.UserAtual.username)  &&
                    MainActivity.users[i].password.equals(MainActivity.UserAtual.password) &&
                    MainActivity.users[i].email.equals(MainActivity.UserAtual.email))
            {

            MainActivity.users[i].password = npassStr;
            Log.i("CHANGE PASSWORD ", "Password trocada\n " );
            }
        }
    }
}