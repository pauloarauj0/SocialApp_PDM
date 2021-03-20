package com.example.socialapp;

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
        nUser++;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
