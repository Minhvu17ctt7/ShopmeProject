package com.example.shopmebackend.brand.repository;

import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
    @Query("SELECT u FROM Brand u WHERE u.name LIKE %?1%")
    Page<Brand> findAll(String keyword, Pageable pageable);
    @Query("SELECT NEW Brand (u.id, u.name) from Brand u ORDER BY u.name ASC")
    List<Brand> findAll();
}
