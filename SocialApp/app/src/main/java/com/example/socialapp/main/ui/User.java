package com.example.socialapp.main.ui;

import android.util.Log;

public class User {
    /**
     * Classe para criar novos usuarios
     * @param username - nome do usuario
     * @param passwrod - senha do usuario
     * @param email - email do usuario
     * @param aniversario - aniversario do usuario
     */
    static int nUser = 0;
    String descricao="";
    String email="";
    String username="";
    String password="";
    String aniversario="";

   private final int INVALID = -1;

    User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    User(String email, String password) {
        this.email = email;
        this. password = password;
    }


    /**
     * Adiciona um novo user ao array de users
     * @return True caso tenha adicionado com sucesso False caso contrario
     */
    public boolean addUser() {
        if(this.findMatch()==INVALID){
            Log.v("CREATED USER", "Novo user criado\n " );
            nUser++;
            MainActivity.users[nUser-1] = this;
            return true;
        }

        return false;
    }

    /**
     * Procurar a posiçao do User no array de users
     * @return Inteiro com o nr da posiçao se exister, -1 caso nao exista
     */
    public int findMatch() {
        for(int i=0;i<User.nUser;i++){
            if(MainActivity.users[i].password.equals(this.password) && MainActivity.users[i].email.equals(this.email)) {
                return i;
            }
        }
        return -1;
    }


/**
 * Altera a senha do usuario
 * @param npassStr  Nova password
 * @return True caso tenha alterado com sucesso False caso contrario
 */
    public boolean changePassword(String npassStr) {

        if(MainActivity.UserAtual.findMatch() != INVALID){
            MainActivity.UserAtual.password = npassStr;

            int i = MainActivity.UserAtual.findMatch();
            MainActivity.users[i] = MainActivity.UserAtual;

            Log.i("CHANGE PASSWORD ", "Password trocada\n " );

            return true;

        }
        return false;
    }

    /**
     * Altera a data de aniversario e guarda a informaçao
     * @param date Data de aniversario
     */

    public void setAniversario(String date) {
        int pos = MainActivity.UserAtual.findMatch();
        if(pos!=INVALID){
            MainActivity.UserAtual.aniversario = date;
            MainActivity.users[pos] = MainActivity.UserAtual;
        }
    }

    /**
     * Altera o nome  e guarda a informaçao
     * @param nName Novo nome
     *@return True caso seja alterado com sucesso
     */
    public boolean changeName(String nName) {
        int pos = MainActivity.UserAtual.findMatch();
        if(pos!=INVALID){
            MainActivity.UserAtual.username = nName;
            MainActivity.users[pos] = MainActivity.UserAtual;
            return true;
        }
        return false;
    }

    /**
     * Altera a descriçao  e guarda a informaçao
     * @param nDesc Nova descriçao
     *@return True caso seja alterado com sucesso
     */
    public boolean changeDesc(String nDesc) {
        int pos = MainActivity.UserAtual.findMatch();
        if(pos!=INVALID){
            MainActivity.UserAtual.descricao = nDesc;
            MainActivity.users[pos] = MainActivity.UserAtual;
            return true;
        }
        return false;
    }

    /**
     * Altera o email  e guarda a informaçao
     * @param nEmail Novo email
     * @return True caso seja alterado com sucesso e False caso ja esteja em uso
     */
    public boolean changeEmail(String nEmail) {
        boolean change = true;
        int pos = MainActivity.UserAtual.findMatch();
        if(pos!=INVALID){
            //verificar se o email nao esta em uso
            for(int i =0; i< nUser; i++){
                if (MainActivity.users[i].email.equals(nEmail)) {
                    change = false;
                    break;
                }
            }
            if(change){
                MainActivity.UserAtual.email = nEmail;
                MainActivity.users[pos] = MainActivity.UserAtual;
                return true;
            }
        }
    return false;
    }
}

