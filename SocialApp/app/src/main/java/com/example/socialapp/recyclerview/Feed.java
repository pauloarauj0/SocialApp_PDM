package com.example.socialapp.recyclerview;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.base.Account;
import com.example.socialapp.data.base.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mAdapter;
    AppDatabase database;


   @Override
   public void onCreate(Bundle savedInstanceState) {
       database = AppDatabase.getDatabase(Feed.this);
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_feed);

       mRecyclerView = findViewById(R.id.recycler_view);
       mAdapter = new RecyclerViewAdapter(database.getDao().getAllPosts(),this);
       mRecyclerView.setAdapter(mAdapter);
       mRecyclerView.setLayoutManager( new LinearLayoutManager(this));


   }


}
