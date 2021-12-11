package com.example.shopmebackend.category.controller;

import com.example.shopmebackend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping("/categories/check-unique")
    public String checkUniqueNameAndAlias(@RequestParam(value = "id", required = false) Long id,
                                          @RequestParam("name") String name, @RequestParam("alias") String alias) {
        return categoryService.checkNameAndAliasUnique(id, name, alias);
    }
}
