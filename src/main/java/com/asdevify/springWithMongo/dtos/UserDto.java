package com.asdevify.springWithMongo.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    String username;

    @NotEmpty
    String password;

    Boolean sentimentAnalysis;

    LocalDate CreateAt;

    List<String> roles;


    
}
