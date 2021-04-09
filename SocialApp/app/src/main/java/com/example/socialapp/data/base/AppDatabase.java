package com.example.socialapp.data.base;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {Account.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao getDao();
    private static AppDatabase instance = null;

    /**
     * Criar a base de dados seguindo o modelo singleton
     * @param context Contexto
     * @return instacia da base de dados
     */

    public synchronized static AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                instance = Room.databaseBuilder(context, AppDatabase.class, "DATABASE").allowMainThreadQueries().build();
            }
        }
        return instance;

    }


}

