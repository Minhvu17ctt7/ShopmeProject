package com.example.shopmebackend.product.controller;

import com.example.shopmebackend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductRestController {

    private final ProductService productService;

    @PostMapping("/products/check-unique")
    public String checkUniqueName(@RequestParam(value = "id", required = false) Long id, @RequestParam String name) {
        return productService.checkNameUnique(id, name);
    }

}
