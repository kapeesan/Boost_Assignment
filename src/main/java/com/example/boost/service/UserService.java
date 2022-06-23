package com.example.boost.service;

import com.example.boost.dto.Comment;
import com.example.boost.dto.CommentList;
import com.example.boost.dto.UserList;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface UserService {
    CompletableFuture<UserList> getAllUser();
    CompletableFuture<CommentList> getAllComment();

    String saveToDatabase() throws ExecutionException, InterruptedException;
}
