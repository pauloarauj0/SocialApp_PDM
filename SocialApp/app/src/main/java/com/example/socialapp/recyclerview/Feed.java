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
import com.example.socialapp.data.base.Posts;
import com.example.socialapp.data.base.UserWithPost;
import com.example.socialapp.main.ui.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feed extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    AppDatabase database;


    private ArrayList<String> getAllNames(){
        List<Account> acc = database.getDao().getAllAccount();
        ArrayList<String> names = new ArrayList<>();
        for(Account x : acc){
            names.add(x.getUserName());
        }
       return names;
    }
    void printNames(ArrayList<String> l){
        for(String s : l){
            Log.i("FEED", s+"  \n");
        }
    }
   @Override
   public void onCreate(Bundle savedInstanceState) {
       database = AppDatabase.getDatabase(Feed.this);
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_feed);
       printNames(getAllNames());

       mRecyclerView = findViewById(R.id.recycler_view);
       mAdapter = new RecyclerViewAdapter(getAllNames(),getApplicationContext());
       mLayoutManager = new LinearLayoutManager(getApplicationContext());
       mRecyclerView.setLayoutManager(mLayoutManager);
       mRecyclerView.setItemAnimator(new DefaultItemAnimator());
       mRecyclerView.setAdapter(mAdapter);

   }


}
