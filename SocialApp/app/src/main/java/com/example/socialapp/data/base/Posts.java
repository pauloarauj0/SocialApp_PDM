package com.example.socialapp.data.base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Posts implements Serializable {
    @PrimaryKey (autoGenerate = true)
    long postID;

    @ColumnInfo
    private String message;

    @ColumnInfo
    private String response;

    @ColumnInfo
    private String post_author;

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
