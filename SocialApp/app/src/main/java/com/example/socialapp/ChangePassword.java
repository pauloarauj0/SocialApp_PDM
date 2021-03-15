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
                //checar se pass coincide com a password atual (depois)
                //checar se npass e npassconf sao iguais
                String passStr, npassStr, npassConfStr;
                passStr = pass.getText().toString();
                npassStr = npass.getText().toString();
                npassConfStr = npassConf.getText().toString();

                if(passStr.isEmpty() || npassStr.isEmpty() || npassConfStr.isEmpty() ){
                    Toast.makeText(ChangePassword.this, "Um dos campos não foi preenchido", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "Parametros vazios" );
                }
                else if(!(npassStr.equals(npassConfStr))){
                    Toast.makeText(ChangePassword.this, "As senhas novas não coincidem ", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "senhas diferentes" + "Senha1: "+passStr + " Senha2 "+ npassStr);

                }
                else{
                    //criar a conta
                    Toast.makeText(ChangePassword.this, "Pass trocado com sucesso ", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "Password trocado com sucesso :\n " );

                    //de fato trocar a nova password
                    //finish();
                }
            }
        });
    }
}