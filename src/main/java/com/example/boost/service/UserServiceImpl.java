package com.example.boost.service;

import com.example.boost.dto.Comment;
import com.example.boost.dto.CommentList;
import com.example.boost.dto.User;
import com.example.boost.dto.UserList;
import com.example.boost.entity.CommentEntity;
import com.example.boost.entity.UserEntity;
import com.example.boost.repository.CommentRepository;
import com.example.boost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Async
    public CompletableFuture<CommentList> getAllComment() {

        String allCommentUrl = "https://dummyapi.io/data/v1/comment?limit=50";

        HttpHeaders headers = new HttpHeaders();
        headers.set("app-id", "62b1ff59ce17862f3eee3931");
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
//        ParameterizedTypeReference<PaginatedResponse<Comment>> responseType = new ParameterizedTypeReference<PaginatedResponse<Comment>>() { };
        //ResponseEntity<RestResponsePage<Comment>> response = restTemplate.exchange(allCommentUrl, HttpMethod.GET, request, responseType);
        ResponseEntity<CommentList> response = restTemplate.exchange(allCommentUrl, HttpMethod.GET, request, CommentList.class);

        if (response.getBody().getTotal() > 50) {

            int page = response.getBody().getTotal() / 50;
            for (int i = 1; i <= page; i++) {
                String allCommentPageUrl = "https://dummyapi.io/data/v1/comment?limit=50&page=" + i;
                ResponseEntity<CommentList> responsePage = restTemplate.exchange(allCommentPageUrl, HttpMethod.GET, request, CommentList.class);
                response.getBody().getData().addAll(responsePage.getBody().getData());
            }

        }

        return CompletableFuture.completedFuture(response.getBody());
    }


    @Override
    @Async
    public CompletableFuture<UserList> getAllUser() {

        String allCommentUrl = "https://dummyapi.io/data/v1/user?limit=50";


        HttpHeaders headers = new HttpHeaders();
        headers.set("app-id", "62b1ff59ce17862f3eee3931");
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
//        ParameterizedTypeReference<PaginatedResponse<Comment>> responseType = new ParameterizedTypeReference<PaginatedResponse<Comment>>() { };
        //ResponseEntity<RestResponsePage<Comment>> response = restTemplate.exchange(allCommentUrl, HttpMethod.GET, request, responseType);
        ResponseEntity<UserList> response = restTemplate.exchange(allCommentUrl, HttpMethod.GET, request, UserList.class);

        if (response.getBody().getTotal() > 50) {

            int page = response.getBody().getTotal() / 50;
            for (int i = 1; i <= page; i++) {
                String allCommentPageUrl = "https://dummyapi.io/data/v1/user?limit=50&page=" + i;
                ResponseEntity<UserList> responsePage = restTemplate.exchange(allCommentPageUrl, HttpMethod.GET, request, UserList.class);
                response.getBody().getData().addAll(responsePage.getBody().getData());
            }

        }

        return CompletableFuture.completedFuture(response.getBody());
    }


    @Override
    public String saveToDatabase() throws ExecutionException, InterruptedException {

        CompletableFuture<UserList> userListCompletableFuture = getAllUser();
        CompletableFuture<CommentList> commentListCompletableFuture = getAllComment();


        List<UserEntity> userEntities = new ArrayList<>();

        for (User user : userListCompletableFuture.get().getData()){
            UserEntity userEntity = new UserEntity();
            userEntity.setId(user.getId());
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setTitle(user.getTitle());
            userEntity.setPicture(user.getPicture());

            userEntities.add(userEntity);

        }

        List<UserEntity> userEntityList = userRepository.saveAll(userEntities);

        commentRepository.deleteAll();

        //add comment
        for(UserEntity userEntity : userEntityList){

            List<CommentEntity> commentEntities = new ArrayList<>();

            for(Comment comment: commentListCompletableFuture.get().getData()){
                if(comment.getOwner().getId().equals(userEntity.getId())){

                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setComment(comment.getMessage());
                    commentEntity.setUser(userEntity);

                    commentEntities.add(commentEntity);
                }

                commentRepository.saveAll(commentEntities);

            }
        }

        return "Data added successfully";
    }

}
