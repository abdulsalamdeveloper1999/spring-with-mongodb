package com.asdevify.springWithMongo.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisImpl {


    @Autowired
    RedisTemplate redisTemplate;

    @Test
     void setMail(){
        redisTemplate.opsForValue().set("email", "asdevify@gmal.com");

        Object object = redisTemplate.opsForValue().get("email");
    }
    
    
}
