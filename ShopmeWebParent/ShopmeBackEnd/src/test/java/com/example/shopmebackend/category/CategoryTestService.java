package com.example.shopmebackend.category;

import com.example.shopmebackend.Category.CategoryRepository;
import com.example.shopmebackend.Category.CategoryService;
import com.example.shopmecommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryTestService {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCheckUniqueNewCategoryWithName() {
        Category category = new Category();
        category.setId(null);
        category.setName("Computer");
        category.setAlias("computer");

        Mockito.when(categoryRepository.findByName("Computer")).thenReturn(category);


        String result = categoryService.checkNameAndAliasUnique(null, "Computer", "computer");
        assertThat(result).isEqualTo("Ok");
    }
}
