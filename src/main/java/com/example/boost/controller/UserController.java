package com.example.boost.controller;

import com.example.boost.dto.Comment;
import com.example.boost.dto.CommentList;
import com.example.boost.dto.UserList;
import com.example.boost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping(value ="/getAllUserAndComments")
    public @ResponseBody
    String getAllUserAndComments() throws ExecutionException, InterruptedException {

        userService.saveToDatabase();

        return "Data added successfully";
    }
}
