package com.example.shopmebackend.user;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(
         String message
    ){
        super(message);
    }
}
