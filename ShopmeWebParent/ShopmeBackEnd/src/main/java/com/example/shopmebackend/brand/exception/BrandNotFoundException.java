package com.example.shopmebackend.brand.exception;

import java.util.function.Supplier;

public class BrandNotFoundException extends Exception {
    public BrandNotFoundException(String message) {
        super(message);
    }
}
