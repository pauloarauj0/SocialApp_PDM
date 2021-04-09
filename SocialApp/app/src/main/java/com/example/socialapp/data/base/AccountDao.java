package com.example.socialapp.data.base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    List<Account> getAllAccount();

    @Insert
    void insertAccount(Account user);

    @Delete
    void deleteUser(Account user);
/*
    @Update
    void updateName(String name);

    @Update
    void updateEmail(String email);

    @Update
    void updateSenha(String senha);

    @Update
    void updateDesc(String desc);

 */


}
