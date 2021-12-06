package com.example.shopmebackend.category;

import com.example.shopmebackend.category.repository.CategoryRepository;
import com.example.shopmecommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepoTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void createRootCategoryTest() {
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void createSubCategoryTest() {
        Category parent = new Category(Long.parseLong("9"));
        Category children = new Category("Memory", parent);
        Category savedCategory = categoryRepository.save(children);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void createManySubCategoryTest() {
        Category parent = new Category(Long.parseLong("4"));
        Category laptopCategory = new Category("Cameras", parent);
        Category componentCategory = new Category("Smartphones", parent);


        List<Category> savedCategories = categoryRepository.saveAll(List.of(laptopCategory, componentCategory));

        assertThat(savedCategories.size()).isGreaterThan(0);
    }

    @Test
    public void getParentCategory() {
        Category category = categoryRepository.getById(Long.parseLong("3"));
        category.getChildren().forEach(cate-> System.out.println(cate.getName() + " "));
    }

    @Test
    public void testPrintAllCategorires() {
        List<Category> categoryList = categoryRepository.findAll();

        for (Category category: categoryList) {
            if(category.getParent() ==null) {
                System.out.println(category.getName());

                printChildren(1, category);
            }
        }
    }

    @Test
    public void printChildren(int level, Category parent) {
        String dotLevel = "";
        for(int i = 0;i < level; i++) {
            dotLevel = dotLevel.concat("--");
        }

        Set<Category> childrenList = parent.getChildren();

        for (Category children :
                childrenList) {
            System.out.print(dotLevel);
            System.out.println(children.getName());

            printChildren(level + 1 ,children);
        }
    }

    @Test
    public void testGetRootCategoryPage() {
        Sort sort = Sort.by("name").ascending();
        Pageable page = PageRequest.of(1, 4, sort);

        Page<Category> pageCate = categoryRepository.findRootCategory(page);

        List<Category> categories = pageCate.getContent();
        System.out.println(categories);
    }

}
