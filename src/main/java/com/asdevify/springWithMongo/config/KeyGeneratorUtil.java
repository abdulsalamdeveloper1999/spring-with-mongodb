package com.asdevify.springWithMongo.config;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



public class KeyGeneratorUtil {
    

public SecretKey  generateKey(){
  try {
     KeyGenerator keyGenerator=KeyGenerator.getInstance("hmacSHA256");
   return keyGenerator.generateKey();

  } catch (NoSuchAlgorithmException e) {
    throw new RuntimeException("Failed to generate key",e);
  }
}

}
