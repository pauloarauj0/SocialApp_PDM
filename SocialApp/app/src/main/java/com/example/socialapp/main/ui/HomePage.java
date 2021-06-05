package com.example.socialapp.main.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.R;
import com.example.socialapp.bluetooth.BluetoothChatHub;
import com.example.socialapp.data.base.AppDatabase;
import com.example.socialapp.data.base.FriendList;
import com.example.socialapp.data.base.Post;
import com.example.socialapp.recyclerview.Feed;

import java.util.Calendar;
import java.util.Date;

public class HomePage extends AppCompatActivity {

    private Button chat;
    private Button post;
    private Button fren;
    private EditText textField;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        database = AppDatabase.getDatabase(HomePage.this);

        chat = findViewById(R.id.home_chat);
        post = findViewById(R.id.post_btn);
        textField = findViewById(R.id.insert_post);

        //debug
        fren = findViewById(R.id.debugFriend);

        fren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendList amigo = new FriendList();
                amigo.setUserName(MainActivity.AccAtual.getUserName());
                for(int i = 0; i<4; i++){
                    amigo.setFriend("Amigo n" + i);
                    database.getDao().insertFriend(amigo);
                }

                Post p = new Post();
                p.setPostID("x");
                p.setPost_author("Amigo n1");
                p.setMessage("I am your n1 friend");

                database.getDao().insertPost(p);

                Post p2 = new Post();
                p2.setPostID("x2");
                p2.setPost_author("n7");
                p2.setMessage("I am your n7 friend");

                database.getDao().insertPost(p2);


                Log.d("HOMEPAGE ", "Amigos de " + MainActivity.AccAtual.getUserName());

                for(String amig : database.getDao().getAllFriends(MainActivity.AccAtual.getUserName())){
                    Log.d("HOMEPAGE ", "Amigo: " + amig);
                }
            }
        });

        //entrar no chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("HOMEPAGE ", "Clicou em Chat");

                Intent intent = new Intent(HomePage.this, BluetoothChatHub.class);
                startActivity(intent);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                Log.v("HOMEPAGE ", "Clicou em Post");
                String message = textField.getText().toString();
                if(message.equals("")){
                    Toast.makeText(HomePage.this, "Post vazio", Toast.LENGTH_SHORT).show();
                }
                else{
                    Post p = new Post();
                    p.setPostID(("" + currentTime + MainActivity.AccAtual.getUserName()).trim().replaceAll(" ", ""));
                    Log.v("HOMEPAGE ", "Post ID: " + ("" + currentTime + MainActivity.AccAtual.getUserName()).trim().replaceAll(" ", ""));
                    p.setPost_author(MainActivity.AccAtual.getUserName());
                    p.setMessage(message);
                    database.getDao().insertPost(p);
                    textField.setText("");
                    Toast.makeText(HomePage.this, "Post publicado", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(HomePage.this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                Intent intent2 = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.change_password:
                Intent intent3 = new Intent(HomePage.this, ChangePassword.class);
                startActivity(intent3);
                return true;
            case R.id.feed:
                Intent intent4 = new Intent(HomePage.this, Feed.class);
                startActivity(intent4);
                return true;
            case R.id.perfil:
                Toast.makeText(HomePage.this, "User: "+MainActivity.AccAtual.getUserName()  +"\n"+ "Email: "+ MainActivity.AccAtual.getEmail()+"\n"+
                "Senha: "+MainActivity.AccAtual.getUserPass() +"\n"+ "Data: "+MainActivity.AccAtual.getUserAniversario()+"\n"
                  +"Descricao: "+ MainActivity.AccAtual.getUserDesc(), Toast.LENGTH_LONG).show();
            default:
                return true;
        }
    }
}

