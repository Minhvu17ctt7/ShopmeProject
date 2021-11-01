package com.example.shopmebackend.admin;

import com.example.shopmebackend.user.UserNotFoundException;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.cert.PolicyNode;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return getCategoriesForShowParent();
    }

    public List<Category> getCategoriesForShowParent() {
        List<Category> categoriesShowForm = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                categoriesShowForm.add(category);

                showChildren(categoriesShowForm, 1, category);
            }
        }
        return categoriesShowForm;
    }


    public void showChildren(List<Category> categoriesShowForm, int level, Category parent) {
        String dotLevel = "";
        for(int i = 0;i < level; i++) {
            dotLevel = dotLevel.concat("--");
        }

        Set<Category> childrenList = parent.getChildren();

        for (Category children :
                childrenList) {
            String name = dotLevel + children.getName();
           children.setName(name);
            categoriesShowForm.add(children);

            showChildren(categoriesShowForm,level + 1 ,children);
        }
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) throws UserNotFoundException {
        Optional<Category> opt = categoryRepository.findById(id);
        return opt.orElseThrow(() -> new UserNotFoundException("Could not found category with id: " + id));
    }
}
