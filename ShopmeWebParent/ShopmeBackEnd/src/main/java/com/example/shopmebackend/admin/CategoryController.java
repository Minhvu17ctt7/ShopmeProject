package com.example.shopmebackend.admin;

import com.example.shopmebackend.user.UserNotFoundException;
import com.example.shopmebackend.utils.FileUploadUtil;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String retrieveAllCategories(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categories", categoryList);
        return "/category/categories";
    }

    @GetMapping("/categories/new")
    public String createCategory(Model model) {
        Category category = new Category();
        List<Category> categoriesForShowParent = categoryService.getCategoriesForShowParent();
        model.addAttribute("category", category);
        model.addAttribute("categories", categoriesForShowParent);
        model.addAttribute("titlePage","Create new category");
        return "/category/categoryForm";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
            RedirectAttributes redirectAttributes
            ,@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        category.setImage(fileName);
        Category savedCategory = categoryService.saveCategory(category);
        String uploadDir = "category-photos/" + savedCategory.getId();
        FileUploadUtil.cleanDir(uploadDir);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        redirectAttributes.addFlashAttribute("message", "The category has been saved successfull!");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        try {
            List<Category> categoriesForShowParent = categoryService.getCategoriesForShowParent();
            model.addAttribute("categories", categoriesForShowParent);

            Category category = categoryService.getCategoryById(id);
            model.addAttribute("category", category);
            return "/category/categoryForm";
        }catch (UserNotFoundException e) {
            model.addAttribute("message",e.getMessage());
            return "category/categories";
        }
    }

}
