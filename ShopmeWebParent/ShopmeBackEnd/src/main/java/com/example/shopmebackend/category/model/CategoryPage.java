package com.example.shopmebackend.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPage {
    private Long totalElements;
    private int totalPages;
}
