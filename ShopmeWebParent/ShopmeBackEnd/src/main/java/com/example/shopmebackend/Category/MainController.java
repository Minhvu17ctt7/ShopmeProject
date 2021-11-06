package com.example.shopmebackend.Category;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String viewPageLogin() {
        return "/login";
    }
}
