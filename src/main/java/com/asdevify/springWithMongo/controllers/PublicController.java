package com.asdevify.springWithMongo.controllers;


import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdevify.springWithMongo.config.JwtUtil;
import com.asdevify.springWithMongo.dtos.UserDto;
import com.asdevify.springWithMongo.entities.UserEntity;
import com.asdevify.springWithMongo.services.UserDetailsImpl;
import com.asdevify.springWithMongo.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public")
@Tag(name = "User Api",description = "Login and Signup Apis")
public class PublicController {


    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserDetailsImpl userDetailsImpl;
    private JwtUtil jwtUtil;

    public PublicController(UserService userService,AuthenticationManager authenticationManager,UserDetailsImpl userDetailsImpl,JwtUtil jwtUtil){
        this.userService=userService;
        this.authenticationManager=authenticationManager;
        this.userDetailsImpl=userDetailsImpl;
        this.jwtUtil=jwtUtil;

        
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDto user) throws Exception {
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


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto user){
        try {
            UserEntity newUser = userService.createUser(user);
            
        return new ResponseEntity<>(newUser,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserDto user){
      try { 
       Authentication auth =   authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        userDetailsImpl.loadUserByUsername(auth.getName());
        jwtUtil.generateToken(auth.getName());
       return new ResponseEntity<>(jwtUtil.generateToken(auth.getName()),HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>("Error occurs",HttpStatus.BAD_REQUEST);
        
      }
    }
    
}
