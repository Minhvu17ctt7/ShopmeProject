package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("titlePage","Create User");
        model.addAttribute("user",user);
        model.addAttribute("listRoles",roles);
        return "userForm";
    }


    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "The user has been saved sussecfull!");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("titlePage", "Edit User (id: "+ id+")");
        model.addAttribute("listRoles",roles);
        try {
            User user = userService.get(id);
             model.addAttribute("user",user);
             return "userForm";
        }catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message",
                    "The user ID " + id+ " has been deleted successfullly");
        }catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Long id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes
    ) {
        userService.updateEnabledStatus(id,enabled);
        String updateStatus = enabled ?  "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + updateStatus;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }
}
