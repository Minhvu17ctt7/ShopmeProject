package com.example.shopmebackend.brand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found brand")
public class BrandNotFoundRestException extends Exception{
}
