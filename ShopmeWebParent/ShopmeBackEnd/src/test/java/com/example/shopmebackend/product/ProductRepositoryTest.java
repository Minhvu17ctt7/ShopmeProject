package com.example.shopmebackend.product;

import com.example.shopmebackend.product.repository.ProductRepository;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import com.example.shopmecommon.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testCreateProduct() {
        Brand brand = testEntityManager.find(Brand.class, 37L);
        Category category = testEntityManager.find(Category.class, 5L);

        Product product = new Product();
        product.setName("Acer nitro 5");
        product.setAlias("Acer_nitro_5");
        product.setShortDescription("A good laptop");
        product.setFullDescription("This is a good laptop, suit for all people");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(1200);
//        product.setCreatedTime(new Date());
//        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(System.out::println);
    }

    @Test
    void testGetProduct() {
        Product product = productRepository.findById(2L).get();
        assertThat(product).isNotNull();
    }

    @Test
    void testUpdateProduct() {
        Product product = productRepository.findById(2L).get();
        product.setName("Asus a17");

        productRepository.save(product);
    }

    @Test
    void testEnabled() {
        productRepository.updateEnabledStatus(1L, false);
    }

    @Test
    void testAddImage() {
        Product product = productRepository.findById(3L).get();

        product.setMainImage("main-image.jpg");

        product.addExtraImage("image-1.jpg");
        product.addExtraImage("image-2.jpg");
        product.addExtraImage("image-3.jpg");

        assertThat(product.getProductImages().size()).isEqualTo(3);
    }

    @Test
    void testAddProductDetail() {
        Product product = productRepository.findById(3L).get();

        product.addDetail("dung luowng", "128gb");
        product.addDetail("camera", "12mb");
        product.addDetail("pin", "5000");

        assertThat(product.getProductDetails().size()).isEqualTo(3);
    }
}
