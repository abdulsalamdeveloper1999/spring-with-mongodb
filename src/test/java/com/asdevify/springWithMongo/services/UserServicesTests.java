package com.asdevify.springWithMongo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asdevify.springWithMongo.entities.UserEntity;

@SpringBootTest
public class UserServicesTests {


    @Autowired
    private UserService userService;
@Disabled
    @Test
    public void findByUsername(){
       assertNotNull( userService.findByUsername("zayn"));
       
    }
    
    @Disabled
    @Test
    public void isEqual(){
        assertEquals(4, 2+2);
    }


    @ParameterizedTest
    @CsvSource("2,2,4")
    public void paramterTest(int a,int b,int expected){
    assertEquals(expected, a+b,"test has been run and result is: ");
    }


    @ParameterizedTest
    @ArgumentsSource(UserArgsmentedProvider.class)
    public void createUserTest(UserEntity user){
        assertNotNull(userService.createUser(user),"user creating fail");;
    }

}
