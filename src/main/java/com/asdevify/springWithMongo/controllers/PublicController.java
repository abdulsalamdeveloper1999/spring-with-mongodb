package com.asdevify.springWithMongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdevify.springWithMongo.entities.UserEntity;
import com.asdevify.springWithMongo.services.UserService;

@RestController
@RequestMapping("/public")
public class PublicController {


    private UserService userService;

    public PublicController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        UserEntity byUsername = userService.findByUsername(user.getUsername());
        if (byUsername!=null) {
            return new ResponseEntity<>("This username has been taken",HttpStatus.FOUND);
        }
        UserEntity createdUser = userService.createUser(user);
        if (createdUser != null) {
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
