package com.example.socialapp.recyclerview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.Post;

public class Feed extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mAdapter;
    AppDatabase database;


   @Override
   public void onCreate(Bundle savedInstanceState) {
       database = AppDatabase.getDatabase(Feed.this);
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_feed);

      // createPosts();

       mRecyclerView = findViewById(R.id.recycler_view);
       mAdapter = new RecyclerViewAdapter(database.getDao().getAllPosts(),this);
       mRecyclerView.setAdapter(mAdapter);
       mRecyclerView.setLayoutManager( new LinearLayoutManager(this));


   }

    private void createPosts() {
       for(int i =0;i<50;i++){
           Post p = new Post();
           p.setPost_author("A"+i);
           p.setMessage("B"+i);
           database.getDao().insertPost(p);
       }
    }


}
