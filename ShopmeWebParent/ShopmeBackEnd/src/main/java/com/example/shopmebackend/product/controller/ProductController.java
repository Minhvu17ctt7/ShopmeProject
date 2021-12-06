package com.example.shopmebackend.product.controller;

import com.example.shopmebackend.brand.service.BrandService;
import com.example.shopmebackend.product.service.ProductService;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
        System.out.println("da vao....." + product);
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Save product successfull");
        return "redirect:/products";
    }

}
