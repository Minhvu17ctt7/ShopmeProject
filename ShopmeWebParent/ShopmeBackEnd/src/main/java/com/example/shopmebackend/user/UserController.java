package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping
    public String viewsAdminHomePage() {
        return "index";
    }

    @GetMapping("/users")
    public String retrieveAllUsers(Model model) {

        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);

        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("user",user);
        model.addAttribute("listRoles",roles);
        return "userForm";
    }


    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        System.out.println(user);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "The user has been saved sussecfull!");
        return "redirect:/users";
    }
}
