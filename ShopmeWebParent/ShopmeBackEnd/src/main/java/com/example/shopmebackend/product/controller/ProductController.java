package com.example.shopmebackend.product.controller;

import com.example.shopmebackend.brand.service.BrandService;
import com.example.shopmebackend.category.exception.CategoryNotFoundException;
import com.example.shopmebackend.common.FileUploadUtil;
import com.example.shopmebackend.product.exception.ProductNotFoundException;
import com.example.shopmebackend.product.service.ProductService;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import com.example.shopmecommon.entity.Product;
import com.example.shopmecommon.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            ,@RequestParam(value = "fileImage", required = false) MultipartFile mainImage,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImages,
                              @RequestParam(value="detailNames", required = false) String[] detailNames,
                              @RequestParam(value = "detailValues", required = false) String[] detailValues,
                              @RequestParam(value = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(value = "imageNames", required = false) String [] imageNames
                              ) throws IOException {
        setMainImage(mainImage, product);
        setExtraImage(extraImages, product);
        setExtraImageExist(product, imageIDs, imageNames);
        setProductDetail(detailNames, detailValues, product);
        Product savedProduct = productService.save(product);
        saveMainImage(mainImage, savedProduct);
        saveExtraImage(extraImages, savedProduct);

        redirectAttributes.addFlashAttribute("message", "Save product successfull");
        return "redirect:/products";
    }

    private void setExtraImageExist(Product product, String[] imageIDs, String[] imageNames) {
        if(imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> productImages = new HashSet<>();

        for(int i = 0; i< imageIDs.length; i++) {
            if(imageIDs[i] != null) {
                Long id = Long.parseLong(imageIDs[i]);
                productImages.add(new ProductImage(id, imageIDs[i], product));
            }
        }
    }

    private void setProductDetail(String[] detailNames, String[] detailValues, Product product) {
        if(detailNames != null && detailNames.length > 0) {
            for (int i =0; i < detailNames.length; i++) {
                String name = detailNames[i];
                String value = detailValues[i];

                if(!name.isEmpty() && !value.isEmpty()) {
                    product.addDetail(name, value);
                }
            }
        }
    }

    private void saveExtraImage(MultipartFile[] extraImages, Product savedProduct) throws IOException {
        if(extraImages.length > 0) {
            for(MultipartFile extraImage: extraImages) {
                if(!extraImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                    String uploadDir = "product-photos/" + savedProduct.getId() + "/extraImages";
                    FileUploadUtil.saveFile(uploadDir, fileName, extraImage);
                }
            }
        }
    }

    private void saveMainImage(MultipartFile mainImage, Product savedProduct) throws IOException {
        if(!mainImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
            String uploadDir = "product-photos/" + savedProduct.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, mainImage);
        }
    }

    private void setExtraImage(MultipartFile[] extraImages, Product product) {
        if(extraImages.length > 0) {
            for(MultipartFile extraImage: extraImages) {
                if(!extraImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                    if(!product.checkFileExist(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

    private void setMainImage(MultipartFile mainImage, Product product) {
        if(!mainImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
            product.setMainImage(fileName);
        }
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
            String mainImageDir = "product-photos/" + id;
            String extraImageDir = "product-photos/" + id +"/extraImages";
            FileUploadUtil.cleanDir(mainImageDir);
            FileUploadUtil.cleanDir(extraImageDir);
            redirectAttributes.addFlashAttribute("message", "The category id " + id + "has been deleted");
        }catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String updateProduct(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            List<Brand> brands = brandService.listAll();
            int sizeExtraImage = product.getProductImages().size();
            int sizeDetail = product.getProductDetails().size();
            model.addAttribute("sizeDetails", sizeDetail);
            model.addAttribute("sizeExtraImage", sizeExtraImage);
            model.addAttribute("product", product);
            model.addAttribute("brands", brands);
            return "/product/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

}
