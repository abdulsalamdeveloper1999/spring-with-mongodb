package com.asdevify.springWithMongo.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    String accessToken;
    String refreshToken;
    
}
