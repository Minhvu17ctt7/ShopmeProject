package com.example.shopmebackend.brand;

import com.example.shopmebackend.brand.repository.BrandRepository;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testAddBrand1() {
        Category category = new Category(6L);
        Brand brand = new Brand("acer");
        brand.getCategories().add(category);

        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testAddBrand2() {
        Category category = new Category(4L);

        Category category2 = new Category(7L);
        Brand brand = new Brand("apple");
        brand.getCategories().add(category);
        brand.getCategories().add(category2);

        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testAddBrand3() {
        Category category = new Category(29L);

        Category category2 = new Category(24L);
        Brand brand = new Brand("samsung");
        brand.getCategories().add(category);
        brand.getCategories().add(category2);

        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll() {
        List<Brand> listBrand = brandRepository.findAll();
        listBrand.forEach(brand -> System.out.println(brand));
        assertThat(listBrand.size()).isGreaterThan(0);
    }

    @Test
    public void testFindBrandById() {
        Optional<Brand> brand = brandRepository.findById(2L);
        assertThat(brand.get()).isNotNull();
    }

    @Test
    public void testFindAllNoPage() {
        List<Brand> brands = brandRepository.findAll();
        System.out.println(brands);
    }


}
