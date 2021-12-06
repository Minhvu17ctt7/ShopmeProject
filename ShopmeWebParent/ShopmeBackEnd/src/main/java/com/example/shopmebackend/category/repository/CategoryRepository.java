package com.example.shopmebackend.category.repository;

import com.example.shopmecommon.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Category findByAlias(String alias);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void updateEnabled(Long id, boolean enabled);

    @Query("SELECT c FROM Category c where c.parent.id is null ")
    Page<Category> findRootCategory(Pageable pageable);

    @Query("SELECT u FROM Category u WHERE u.name LIKE %?1%")
    public Page<Category> findAll(String keyword, Pageable pageable);
}
