package com.example.shopmebackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/users/check_email")
    public String checkEmailUnique(@Param("email") String email) {
        boolean isUnique = userService.isEmailUnique(email);
        return isUnique? "OK":"Duplicated";
    }

}
