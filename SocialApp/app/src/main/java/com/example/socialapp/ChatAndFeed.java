package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                Intent intent = new Intent(ChatAndFeed.this, BluetoothChatHub.class);
                startActivity(intent);
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