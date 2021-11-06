package com.example.shopmebackend.Category;

import com.example.shopmebackend.user.UserNotFoundException;
import com.example.shopmebackend.utils.FileUploadUtil;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
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
    public String retrieveAllCategories(Model model, @RequestParam(value = "sorted", required = false) String sorted) {
        List<Category> categoryList = categoryService.getAllCategories(sorted);
        String sortRever = (sorted != null && sorted.equals("desc") ) ? "asc" : "desc";
        model.addAttribute("categories", categoryList);
        model.addAttribute("sortReverse", sortRever);
        return "/category/categories";
    }

    @GetMapping("/categories/new")
    public String createCategory(Model model) {
        Category category = new Category();
        List<Category> categoriesForShowParent = categoryService.getCategoriesForShowParent("asc");
        model.addAttribute("category", category);
        model.addAttribute("listCategories", categoriesForShowParent);
        model.addAttribute("titlePage","Create new category");
        return "/category/categoryForm";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
            RedirectAttributes redirectAttributes
            ,@RequestParam(value = "fileImage", required = false) MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory = categoryService.saveCategory(category);
            String uploadDir = "category-photos/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            if(category.getImage().isEmpty()) category.setImage(null);
            categoryService.saveCategory(category);
        }
        redirectAttributes.addFlashAttribute("message", "The category has been saved successfull!");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        try {
            Category category = categoryService.getCategoryById(id);
            model.addAttribute("category", category);
            List<Category> categoriesForShowParent = categoryService.getCategoriesForShowParent("asc");
            model.addAttribute("listCategories", categoriesForShowParent);
            model.addAttribute("titlePage","Update category");
            return "/category/categoryForm";
        }catch (UserNotFoundException e) {
            model.addAttribute("message",e.getMessage());
            return "category/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{enabled}")
    public String updateEnabled(@PathVariable Long id, @PathVariable boolean enabled, RedirectAttributes redirectAttributes) {
        categoryService.updateEnabled(id, enabled);
        String enable = enabled ? "enables" : "disabled";
        String message = "The category ID " + id + "has been " + enable;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            String imageDir = "category-photos/" + id;
            FileUploadUtil.cleanDir(imageDir);
            redirectAttributes.addFlashAttribute("message", "The category id " + id + "has been deleted");
        }catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }
}
