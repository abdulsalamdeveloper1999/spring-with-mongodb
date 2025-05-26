package com.asdevify.springWithMongo.services;

import com.asdevify.springWithMongo.dtos.UserDto;
import com.asdevify.springWithMongo.entities.UserEntity;
import com.asdevify.springWithMongo.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    
    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity createUser(UserDto user) throws Exception{
       try {
       UserEntity byUsername = userRepo.findByUsername(user.getUsername());
        if (byUsername!=null) {
            throw new Exception("user already exist");
        }

        UserEntity userEntity=new UserEntity();

        userEntity.setCreateAt(LocalDate.now());
        userEntity.setRoles(user.getRoles());
        
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("user has been created"+userEntity);

        return userRepo.save(userEntity);
       } catch (Exception e) {
        log.error(e.getMessage());
        throw new RuntimeException("Error:---"+e.getMessage());
       }

    }

    public boolean deleteUser(ObjectId id) {
        Optional<UserEntity> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return true;
        }
        return false;
    }

    

    public UserEntity updateUser(UserEntity user) throws Exception {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity oldUser = userRepo.findByUsername(username);

        if (oldUser != null) {
            // Update only non-sensitive fields (e.g., username)
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                oldUser.setUsername(user.getUsername());
            }

            // Only update password if it's explicitly provided AND different
            if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                    !passwordEncoder.matches(user.getPassword(), oldUser.getPassword())) {
                oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            return userRepo.save(oldUser);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    } catch (Exception e) {
        throw new IllegalAccessException(e.toString());
    }
}


    public UserEntity findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public UserEntity findById(ObjectId id) {
        return userRepo.findById(id).orElse(null);

    }

    public List<UserEntity> getUsers(){
       return userRepo.findAll();
    }

    

}
