package com.example.shopmebackend.brand.controller;

import com.example.shopmebackend.brand.exception.BrandNotFoundRestException;
import com.example.shopmebackend.brand.model.CategoryDTO;
import com.example.shopmebackend.brand.service.BrandService;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandRestController {

    private final BrandService brandService;

    @PostMapping("/check-unique")
    public boolean checkUniqueName(@RequestParam(name ="name") String name, @RequestParam(name = "id",required = false) Long id) {
        return brandService.checkUniqueName(id, name);
    }

    @GetMapping("/{id}/categories")
    public List<CategoryDTO> getListCategoriesByBrand(@PathVariable(name = "id") Long id) throws BrandNotFoundRestException {
        List<CategoryDTO> categories = new ArrayList<>();
        try {
            Brand brand = brandService.getBrandByIdRest(id);
            Set<Category> categoriesList = brand.getCategories();

            categoriesList.forEach(category -> {
                CategoryDTO categoryDTO = new CategoryDTO();
                BeanUtils.copyProperties(
                        category,categoryDTO
                );
                categories.add(categoryDTO);
            });
        }catch (BrandNotFoundRestException brandNotFoundRestException) {
            throw new BrandNotFoundRestException();
        }
        return categories;
    }
}
