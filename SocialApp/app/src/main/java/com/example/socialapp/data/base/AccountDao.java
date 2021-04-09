package com.example.socialapp.data.base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    List<Account> getAllAccount();

    @Query("SELECT * FROM account WHERE email LIKE :input_email and user_senha LIKE :input_senha" )
    int findAccount(String input_email, String input_senha);

    //return -1 caso nao consiga colocar
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertAccount(Account user);

    @Delete
    void deleteAccount(Account user);

    @Delete
    void reset(List<Account> accounts);

    @Query("DELETE FROM account")
    void deleteDatabase();

    //return -1 caso nao consiga colocar
    @Update (onConflict = OnConflictStrategy.IGNORE)
    int updateAccount(Account account);



}
