package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ChatAndFeed extends AppCompatActivity {
    private Button feed, chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_and_feed);

        feed = findViewById(R.id.feedBtn);
        chat = findViewById(R.id.chatBtn);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("CHATANDFEED ", "Clicou em CHAT");
            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("CHATANDFEED ", "Clicou em FEED");
            }
        });

    }
}