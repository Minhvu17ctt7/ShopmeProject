package com.example.shopmebackend.Category;

public class CategoryNotFoundException  extends Exception {
    public CategoryNotFoundException(
            String message
    ){
        super(message);
    }
}
