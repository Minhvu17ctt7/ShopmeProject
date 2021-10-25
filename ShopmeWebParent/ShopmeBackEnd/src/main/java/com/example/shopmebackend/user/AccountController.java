package com.example.shopmebackend.user;

import com.example.shopmebackend.security.ShopmeUserDetail;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AccountController {
    private final UserService userService;

    @GetMapping("/account")
    public String viewDetail(@AuthenticationPrincipal ShopmeUserDetail shopmeUserDetail,
                             Model model) {
        String email = shopmeUserDetail.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user",user);
        return "account-detail";
    }
}
