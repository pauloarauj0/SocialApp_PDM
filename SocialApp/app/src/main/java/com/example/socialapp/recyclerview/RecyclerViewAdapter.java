package com.example.socialapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.Post;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Post> posts = new ArrayList<>();
    private Context context;
    AppDatabase database;

    public RecyclerViewAdapter(List<Post> posts,Context context) {
        this.posts = posts;
        this.context = context;
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
        Post current = posts.get(position);

        holder.owner.setText(current.getPost_author());
        holder.post_content.setText(current.getMessage());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView owner;
        TextView post_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            owner = itemView.findViewById(R.id.post_owner);
            post_content = itemView.findViewById(R.id.post);


        }
    }
}
