package com.example.shopmebackend;

import com.example.shopmebackend.category.service.CategoryService;
import com.example.shopmebackend.user.exception.UserNotFoundException;
import com.example.shopmecommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopmeWebParentApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetCategoryById() {
        try {
            Category category = categoryService.getCategoryById(Long.parseLong("8"));
            System.out.println("category");
            System.out.println(category.getName());
        }catch (UserNotFoundException e) {
            System.out.println("err");
            System.out.println(e.getMessage());
        }
    }
}
