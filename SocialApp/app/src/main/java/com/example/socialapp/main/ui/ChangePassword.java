package com.example.socialapp.main.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialapp.R;

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
                    Toast.makeText(ChangePassword.this, "Password nova não coincide ", Toast.LENGTH_SHORT).show();
                    Log.d("CHANGEPASSWORD", "senhas diferentes" + "Senha1: "+passStr + " Senha2 "+ npassStr);

                }
                //trocar senhas
                else{
                    if(MainActivity.UserAtual.changePassword(npassStr)){
                        Toast.makeText(ChangePassword.this, "Password trocado com sucesso ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(ChangePassword.this, "ERRO: Password não foi alterada ", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }


}