package com.example.shopmebackend.category.exception;

public class CategoryNotFoundException  extends Exception {
    public CategoryNotFoundException(
            String message
    ){
        super(message);
    }
}
