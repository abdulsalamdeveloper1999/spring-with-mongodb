package com.asdevify.springWithMongo.services;

import com.asdevify.springWithMongo.entities.UserEntity;
import com.asdevify.springWithMongo.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsImpl implements UserDetailsService {

    

    
    private UserRepo userRepo;

    public UserDetailsImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);

        if (username == null || username.trim().isEmpty()) {
            log.warn("Username is null or empty");
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        try {
            UserEntity user = userRepo.findByUsername(username);

            if (user == null) {
                log.warn("No user found with username: {}", username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            log.info("User found: {}", user.getUsername());
            log.info("User roles: {}", user.getRoles());
            log.info("User encoded password: {}", user.getPassword());

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0])) // DO NOT prefix with ROLE_
                    .build();
        } catch (Exception e) {
            log.error("Exception while loading user details for username: {}", username, e);
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
