package com.example.shopmebackend.category.service;

import com.example.shopmebackend.category.model.CategoryPage;
import com.example.shopmebackend.category.exception.CategoryNotFoundException;
import com.example.shopmebackend.category.repository.CategoryRepository;
import com.example.shopmebackend.user.exception.UserNotFoundException;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final int CATE_NUMBER_ITEM = 4;
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories(String sorted) {
        return getCategoriesForShowParent(sorted);
    }

    public List<Category> getCategoryPage(int page, String sorted, String keyword, CategoryPage catePage) {
        Sort sort = Sort.by("name");

        if(sorted != null && sorted.equals("desc")) {
            sort = sort.descending();
        }else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page - 1, CATE_NUMBER_ITEM, sort);

        Page<Category> categoryPage = null;
        if(keyword != null && !keyword.isEmpty()) {
            categoryPage = categoryRepository.findAll(keyword, pageable);
        } else {
            categoryPage = categoryRepository.findRootCategory(pageable);
        }



        catePage.setTotalElements(categoryPage.getTotalElements());
        catePage.setTotalPages(categoryPage.getTotalPages());

        if(keyword != null && !keyword.isEmpty()) {
            List<Category> searchResult = categoryPage.getContent();
            for (Category tempCate :
                    searchResult) {
                tempCate.setHasChildren(false);
            }
            return searchResult;
        }
            return getCategoriesForShowParentPage(categoryPage.getContent(), sorted);
    }

    public List<Category> getCategoriesForShowParentPage(List<Category> categories, String sorted) {
        return getCategoriesSort(categories, sorted);
    }

    private List<Category> getCategoriesSort(List<Category> categories, String sorted) {
        List<Category> categoriesShowForm = new ArrayList<>();
        for (Category category : categories) {
            if (category.getParent() == null) {
                Category parentCate = new Category();
                BeanUtils.copyProperties(category, parentCate);
                parentCate.updateHasChildren();
                categoriesShowForm.add(parentCate);

                showChildren(categoriesShowForm, 1, category, sorted);
            }
        }
        return categoriesShowForm;
    }

    public List<Category> getCategoriesForShowParent(String sorted) {

        Sort sort = Sort.by("name");

        if(sorted != null && sorted.equals("desc")) {
            sort = sort.descending();
        }else {
            sort = sort.ascending();
        }
        List<Category> categories =  categoryRepository.findAll(sort);

        return getCategoriesSort(categories, sorted);
    }


    public void showChildren(List<Category> categoriesShowForm, int level, Category parent,  String sorted) {
        String dotLevel = "";
        for(int i = 0;i < level; i++) {
            dotLevel = dotLevel.concat("--");
        }

        Set<Category> childrenList = sortCategory(parent.getChildren(), sorted);

        for (Category children :
                childrenList) {
            String name = dotLevel + children.getName();
            Category newCate = new Category();
            BeanUtils.copyProperties(children, newCate);
            newCate.setName(name);
            categoriesShowForm.add(newCate);
            newCate.updateHasChildren();

            showChildren(categoriesShowForm,level + 1 ,children, sorted);
        }
    }

    public SortedSet<Category> sortCategory(Set<Category> categories, String sorted) {
        SortedSet<Category> sortedCategory = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category c1, Category c2) {
                if(sorted != null && sorted.equals("desc")) {
                    return c2.getName().compareTo(c1.getName());
                } else {
                    return c1.getName().compareTo(c2.getName());
                }
            }
        });
        sortedCategory.addAll(categories);
        return sortedCategory;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) throws UserNotFoundException {
        Optional<Category> opt = categoryRepository.findById(id);
        if(opt.isEmpty()) {
            throw new UserNotFoundException("Could not found category with id: " + id);
        }
        System.out.println(opt.get().toString());
        return opt.get();
    }

    public String checkNameAndAliasUnique(Long id, String name, String alias) {
        boolean isNewCategory = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);
        if(isNewCategory) {
            if(categoryByName != null) {
                return "DuplicationName";
            }else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if(categoryByAlias != null) {
                    return "DuplicationAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicationName";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null && categoryByAlias.getId() != id) {
                    return "DuplicationAlias";
                }
            }
        }
        return "Ok";
    }

    public void updateEnabled(Long id, boolean enabled) {
        categoryRepository.updateEnabled(id, enabled);
    }

    public void deleteCategory(Long id) throws CategoryNotFoundException {
        Optional<Category> opt = categoryRepository.findById(id);
        if(opt.isEmpty()) {
            throw new CategoryNotFoundException("Could not found category with id " + id);
        }
        categoryRepository.deleteById(id);
    }
}
