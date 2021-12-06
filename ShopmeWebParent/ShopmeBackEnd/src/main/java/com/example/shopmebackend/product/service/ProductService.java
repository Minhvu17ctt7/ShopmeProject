package com.example.shopmebackend.product.service;

import com.example.shopmebackend.product.repository.ProductRepository;
import com.example.shopmecommon.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
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
}
