package com.example.socialapp.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.UserWithPost;
import com.example.socialapp.main.ui.MainActivity;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> p_owner;
    private Context context;
    private List<UserWithPost> user_post = new ArrayList<>();
    AppDatabase database;

    public RecyclerViewAdapter(ArrayList<String> p_owner,Context context) {
        this.p_owner = p_owner;
        this.context = context;
    }

    private void getUserPost(){
        for(String x : p_owner){
            user_post = database.getDao().getUserPost(x);

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        database = AppDatabase.getDatabase(context);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getUserPost();
        UserWithPost current = user_post.get(position);

        holder.owner.setText(current.user.getUserName());
        holder.post_content.setText(current.posts.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return user_post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView owner;
        TextView post_content;
        RecyclerView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            owner = itemView.findViewById(R.id.post_owner);
            post_content = itemView.findViewById(R.id.post);
            parent = itemView.findViewById(R.id.parent_layout);


        }
    }
}
