package com.example.socialapp;

import android.util.Log;

public class User {
    /**
     * Classe para criar novos usuarios
     * @param username - nome do usuario
     * @param passwrod - senha do usuario
     * @param email - email do usuario
     */
    static int nUser = 0;
    String email="";
    String username="";
    String password="";

    User(String username, String password, String email){
        Log.v("CREATED USER", "Novo user criado\n " );
        nUser++;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    User(){

    }
}
