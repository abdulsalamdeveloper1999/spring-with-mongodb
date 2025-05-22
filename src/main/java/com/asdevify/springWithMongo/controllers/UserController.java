package com.asdevify.springWithMongo.controllers;

import com.asdevify.springWithMongo.entities.UserEntity;
import com.asdevify.springWithMongo.entities.WeatherPojo;
import com.asdevify.springWithMongo.services.UserService;
import com.asdevify.springWithMongo.services.WeatherService;

import org.bson.types.ObjectId;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {



    private UserService userService; 
    private WeatherService weatherService;

    public UserController(UserService userService,WeatherService weatherService) {
        this.userService = userService;
        this.weatherService=weatherService;

    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable ObjectId id){
      try {
         boolean isDeleted= userService.deleteUser(id);
       if (isDeleted){
           return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
       }else{
           return new ResponseEntity<>("Error while deleting user", HttpStatus.BAD_REQUEST);
       }
      } catch (Exception e) {
      return new ResponseEntity<>("User not authroized", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
      }
    }

    @PutMapping("update-user")    
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user) throws Exception{
        UserEntity updatedUser= userService.updateUser(user);
        if (updatedUser!=null){
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    

    @GetMapping("greet-user")
    public ResponseEntity<String> greetUser(@RequestParam String city){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        WeatherPojo weather = weatherService.getWeather(city);
        if (weather!=null) {
            return new ResponseEntity<>(String.format("Welcome %s %s feels like %d",name,city,weather.getCurrent().getFeelslike()),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Weather not found",HttpStatus.NOT_FOUND);
        }

    }


}
