package com.example.socialapp.data.base;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account implements Serializable {
    @PrimaryKey @NonNull @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "user_name")
     private String userName;

    @ColumnInfo(name = "user_desc")
     private String userDesc;

    @ColumnInfo(name = "user_senha")
     private String userPass;

    @ColumnInfo(name = "user_aniversario")
    private String userAniversario;

    @ColumnInfo(name = "user_post")
    private int userPostID;


    public int getUserPostID() {
        return userPostID;
    }

    public void setUserPostID(int userPostID) {
        this.userPostID = userPostID;
    }

    public String getUserAniversario() {
        return userAniversario;
    }

    public void setUserAniversario(String userAniversario) {
        this.userAniversario = userAniversario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}


