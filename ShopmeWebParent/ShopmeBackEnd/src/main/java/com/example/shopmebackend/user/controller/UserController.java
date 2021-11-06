package com.example.shopmebackend.user.controller;

import com.example.shopmebackend.user.UserNotFoundException;
import com.example.shopmebackend.user.UserService;
import com.example.shopmebackend.utils.FileUploadUtil;
import com.example.shopmebackend.utils.UserCsvExporter;
import com.example.shopmebackend.utils.UserExcelExporter;
import com.example.shopmebackend.utils.UserPdfExporter;
import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public String retrieveUsersFirstPage(Model model) {
        return retrieveUsersPagination(model,1, "firstName", "asc", null);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String retrieveUsersPagination(Model model, @PathVariable("pageNumber") int pageNumber,@RequestParam("sortField") String sortField,
            @RequestParam("sortName") String sortName, @RequestParam("keyword") String keywork) {
        Page<User> page = userService.getListUserPagination(pageNumber, sortField, sortName, keywork);
        List<User> users = page.getContent();

        Long startCount = Long.valueOf((pageNumber - 1) * UserService.USER_SIZE_PAGE + 1);
        Long endCount = startCount + UserService.USER_SIZE_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSort = sortName.equals("asc") ? "dsc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("users",users);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortName", sortName);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("keyword", keywork);

        return "user/users";
    }


    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("titlePage","Create User");
        model.addAttribute("user",user);
        model.addAttribute("listRoles",roles);
        return "user/userForm";
    }


    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.saveUser(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.saveUser(user);
        }
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfull!");
        return getRedirectUrlFromUser(user);
    }

    private String getRedirectUrlFromUser(User user) {
        String nameInEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortName=asc&keyword=" + nameInEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("titlePage", "Edit User (id: "+ id+")");
        model.addAttribute("listRoles",roles);
        try {
            User user = userService.get(id);
             model.addAttribute("user",user);
             return "/user/userForm";
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

    @GetMapping("users/export/csv")
    public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers = userService.getAllUsers();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, httpServletResponse);
    }

    @GetMapping("users/export/excel")
    public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers = userService.getAllUsers();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, httpServletResponse);
    }

    @GetMapping("users/export/pdf")
    public void exportToPdf(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers = userService.getAllUsers();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, httpServletResponse);
    }


}
