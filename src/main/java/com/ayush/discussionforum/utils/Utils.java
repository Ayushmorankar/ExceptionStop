package com.ayush.discussionforum.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Component
public class Utils {

    private final Random random = new SecureRandom();

    public String generatePublicStudentId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){

        String ALPHABETS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder id = new StringBuilder();

        for(int i = 0; i<length; i++){
            id.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
        }
        return id.toString();
    }

    public String generateRefreshTokenString(){
        return UUID.randomUUID().toString();
    }
}
