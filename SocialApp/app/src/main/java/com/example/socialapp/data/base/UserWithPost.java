package com.example.socialapp.data.base;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithPost {
    @Embedded public Account user;
    @Relation(
            parentColumn = "user_name",
            entityColumn = "post_author"
    )
    public List<Posts> posts;
}
