package com.example.socialapp.data.base;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey @NonNull String email;
    
    String senha;

    public Account(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    @ColumnInfo(name = "user_email")
     String userEmail;

    @ColumnInfo(name = "user_name")
     String userName;

    @ColumnInfo(name = "user_desc")
     String userDesc;

    @ColumnInfo(name = "user_senha")
     String userPass;


/*
    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setPrimaryKey(String email) {
        this.email = email;
    }

 */
}
