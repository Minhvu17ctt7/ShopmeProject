package com.example.shopmebackend.user.controller;

import com.example.shopmebackend.security.ShopmeUserDetail;
import com.example.shopmebackend.user.service.UserService;
import com.example.shopmebackend.common.FileUploadUtil;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

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
        return "user/account-form";
    }

    @PostMapping("/account/update")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ShopmeUserDetail shopmeUserDetail,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateUser(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateUser(user);
        }
        shopmeUserDetail.setFirstName(user.getFirstName());
        shopmeUserDetail.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "The user has been updated!");
        return "redirect:/account";
    }
}
