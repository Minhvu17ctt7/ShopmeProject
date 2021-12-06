package com.example.shopmebackend.category.controller;

import com.example.shopmebackend.category.export.CategoryExporter;
import com.example.shopmebackend.category.model.CategoryPage;
import com.example.shopmebackend.category.service.CategoryService;
import com.example.shopmebackend.category.exception.CategoryNotFoundException;
import com.example.shopmebackend.user.exception.UserNotFoundException;
import com.example.shopmebackend.common.FileUploadUtil;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String retrieveAllCategories(Model model, @RequestParam(value = "sorted", required = false) String sorted) {
//        List<Category> categoryList = categoryService.getAllCategories(sorted);
//        String sortRever = (sorted != null && sorted.equals("desc") ) ? "asc" : "desc";
//        model.addAttribute("categories", categoryList);
//        model.addAttribute("sortReverse", sortRever);
        return retrieveCategoriesPage(null,null, 1, model);
    }

    @GetMapping("/categories/page/{numberPage}")
    public String retrieveCategoriesPage(@RequestParam(value = "sortField", required = false) String sortField,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @PathVariable(value = "numberPage") int numberPage,Model model) {
        CategoryPage categoryPage = new CategoryPage();
        List<Category> categoryList = categoryService.getCategoryPage(numberPage, sortField, keyword, categoryPage);
        String sortRever = (sortField != null && sortField.equals("desc") ) ? "asc" : "desc";

        model.addAttribute("currentPage", numberPage);
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortName", "name");
        model.addAttribute("categories", categoryList);
        model.addAttribute("reverseSort", sortRever);

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

    @GetMapping("/categories/export/csv")
    public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<Category> listCategories = categoryService.getAllCategories("asc");
        CategoryExporter exporter = new CategoryExporter();
        exporter.export(listCategories, httpServletResponse);
    }
}
