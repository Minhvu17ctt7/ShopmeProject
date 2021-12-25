package com.example.shopmebackend.product.service;

import com.example.shopmebackend.category.exception.CategoryNotFoundException;
import com.example.shopmebackend.product.exception.ProductNotFoundException;
import com.example.shopmebackend.product.repository.ProductRepository;
import com.example.shopmecommon.entity.Category;
import com.example.shopmecommon.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        if(product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if(product.getAlias() == null || product.getAlias().isEmpty()) {
            String aliasDefault = product.getName().replaceAll(" ", "-");
            product.setAlias(aliasDefault);
        }else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }

    public String checkNameUnique(Long id, String name) {
        Boolean isProductNew = (id == null || id ==0);
        Product product = productRepository.findByName(name);
        if(isProductNew) {
            if(product != null) return "DUPLICATE";
        }else {
            if(product != null && product.getId() != id)
                return "DUPLICATE";
        }
        return "OK";
    }

    public void updateEnabledStatus(Long id, boolean enabled) {
        System.out.println("id: " + id);
        System.out.println("enabled: " + enabled);
        productRepository.updateEnabledStatus(id, enabled);
    }

    public void deleteProduct(Long id) throws ProductNotFoundException {
        Optional<Product> opt = productRepository.findById(id);
        if(opt.isEmpty()) {
            throw new ProductNotFoundException("Could not found product with id " + id);
        }
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() ->
                 new ProductNotFoundException("Not found product with id: "+ id));
    }
}
