package com.example.shopmebackend.product.controller;

import com.example.shopmebackend.brand.service.BrandService;
import com.example.shopmebackend.category.exception.CategoryNotFoundException;
import com.example.shopmebackend.common.FileUploadUtil;
import com.example.shopmebackend.product.exception.ProductNotFoundException;
import com.example.shopmebackend.product.service.ProductService;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import com.example.shopmecommon.entity.Product;
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
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final BrandService brandService;
    private final ProductService productService;

    @GetMapping
    public String retrieveAllProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/product/products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<Brand> brands = brandService.listAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("titlePage", "Create new product");
        return "/product/product_form";
    }

    @PostMapping("/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes
            ,@RequestParam(value = "fileImage", required = false) MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setMainImage(fileName);
            Product savedProduct = productService.save(product);
            String uploadDir = "product-photos/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            productService.save(product);
        }
        redirectAttributes.addFlashAttribute("message", "Save product successfull");
        return "redirect:/products";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateEnabledProduct(@PathVariable(name = "id") Long id, @PathVariable(name ="status") boolean enabled,
                                       RedirectAttributes redirectAttributes) {
        productService.updateEnabledStatus(id, enabled);
        String updateStatus = enabled ?  "enabled" : "disabled";
        String message = "The product ID " + id + " has been " + updateStatus;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
//            String imageDir = "category-photos/" + id;
//            FileUploadUtil.cleanDir(imageDir);
            redirectAttributes.addFlashAttribute("message", "The category id " + id + "has been deleted");
        }catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/products";
    }

}
