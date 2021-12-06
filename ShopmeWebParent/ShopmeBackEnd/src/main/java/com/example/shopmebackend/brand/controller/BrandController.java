package com.example.shopmebackend.brand.controller;

import com.example.shopmebackend.brand.exception.BrandNotFoundException;
import com.example.shopmebackend.brand.model.BrandPage;
import com.example.shopmebackend.brand.service.BrandService;
import com.example.shopmebackend.category.model.CategoryPage;
import com.example.shopmebackend.category.service.CategoryService;
import com.example.shopmebackend.common.FileUploadUtil;
import com.example.shopmebackend.user.exception.UserNotFoundException;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;
    @GetMapping
    public String retrieveAllBrands(Model model) {
        return retrieveBrandPage(null,null, 1, model);
    }

    @GetMapping("/page/{numberPage}")
    public String retrieveBrandPage(@RequestParam(value = "sortField", required = false) String sortField,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @PathVariable(value = "numberPage") int numberPage,Model model) {
        BrandPage brandPage = new BrandPage();
        List<Brand> brandList = brandService.getBrandPage(numberPage, sortField, keyword, brandPage);
        String sortRever = (sortField != null && sortField.equals("desc") ) ? "asc" : "desc";

        model.addAttribute("currentPage", numberPage);
        model.addAttribute("totalItems", brandPage.getTotalElements());
        model.addAttribute("totalPages", brandPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortName", "name");
        model.addAttribute("brands", brandList);
        model.addAttribute("reverseSort", sortRever);

        return "/brand/brands";
    }

    @GetMapping("/new")
    public String createBrand(Model model) {
        Brand brand = new Brand();
        List<Category> categoriesForShowParent = categoryService.getCategoriesForShowParent("asc");
        model.addAttribute("brand", brand);
        model.addAttribute("listCategories", categoriesForShowParent);
        model.addAttribute("titlePage","Create new brand");
        return "/brand/brandForm";
    }

    @PostMapping("/save")
    public String saveBrand(Brand brand,
                               RedirectAttributes redirectAttributes
            , @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setPhoto(fileName);
            Brand savedBrand = brandService.saveBrand(brand);
            String uploadDir = "brand-photos/" + savedBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            if(brand.getPhoto().isEmpty()) brand.setPhoto(null);
            brandService.saveBrand(brand);
        }
        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully!");
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String editBrand(@PathVariable Long id, Model model) {
        try {
            Brand brand = brandService.getBrandById(id);
            model.addAttribute("brand", brand);
            List<Category> categoriesForShowParent = categoryService.getCategoriesForShowParent("asc");
            model.addAttribute("listCategories", categoriesForShowParent);
            model.addAttribute("titlePage","Update category");
            return "brand/brandForm";
        }catch (BrandNotFoundException e) {
            model.addAttribute("message",e.getMessage());
            return "brand/brands";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            brandService.deleteBrand(id);
            String dirImage = "brand-photos/" + id;
            FileUploadUtil.cleanDir(dirImage);
            redirectAttributes.addFlashAttribute("message", "Deleted brand with id:" + id+" successful");
            return "redirect:/brands";
        }catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }
}
