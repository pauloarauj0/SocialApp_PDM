package com.example.socialapp.recyclerview;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.FriendList;
import com.example.socialapp.data.base.Post;
import com.example.socialapp.main.ui.MainActivity;

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

      // createPosts();
       List<Post> vPosts = friendPosts(database.getDao().getAllPosts());

       mRecyclerView = findViewById(R.id.recycler_view);
       mAdapter = new RecyclerViewAdapter(vPosts,this);
       mRecyclerView.setAdapter(mAdapter);
       mRecyclerView.setLayoutManager( new LinearLayoutManager(this));


   }

   public List<Post> friendPosts(List<Post> posts){
       List<Post> fposts = new ArrayList<>();
       Boolean isFriend = false;
       for(Post p: posts){
           isFriend = false;
           for(String amig : database.getDao().getAllFriends(MainActivity.AccAtual.getUserName())){
               if(amig.equals(p.getPost_author())){
                   isFriend = true;
                   break;
               }
           }
           if(isFriend){
               fposts.add(p);
               Log.d("FEED", "Adding this post cause we're friends.");
           }
           if(MainActivity.AccAtual.getUserName().equals(p.getPost_author())){
               fposts.add(p);
               Log.d("FEED", "Adding this post cause I made it");
           }
       }
       return fposts;
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
