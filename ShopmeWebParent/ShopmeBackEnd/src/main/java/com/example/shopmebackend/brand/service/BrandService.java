package com.example.shopmebackend.brand.service;

import com.example.shopmebackend.brand.exception.BrandNotFoundException;
import com.example.shopmebackend.brand.exception.BrandNotFoundRestException;
import com.example.shopmebackend.brand.model.BrandPage;
import com.example.shopmebackend.brand.repository.BrandRepository;
import com.example.shopmebackend.category.model.CategoryPage;
import com.example.shopmecommon.entity.Brand;
import com.example.shopmecommon.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final static int BRAND_NUMBER_ITEM = 4;

    public List<Brand> getAllCategory() {
        return brandRepository.findAll();
    }
    public List<Brand> getBrandPage(int page, String sorted, String keyword, BrandPage brandPage) {
        Sort sort = Sort.by("name");

        if(sorted != null && sorted.equals("desc")) {
            sort = sort.descending();
        }else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page - 1, BRAND_NUMBER_ITEM, sort);

        Page<Brand> brandPageList = null;
        if(keyword != null && !keyword.isEmpty()) {
            brandPageList = brandRepository.findAll(keyword, pageable);
        } else {
            brandPageList = brandRepository.findAll(pageable);
        }
        brandPage.setTotalElements(brandPageList.getTotalElements());
        brandPage.setTotalPages(brandPageList.getTotalPages());

        return brandPageList.getContent();
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }
    public Brand getBrandById(Long id) throws BrandNotFoundException {
        Optional<Brand> optBrand = brandRepository.findById(id);
        if(optBrand.isEmpty()) {
            throw new BrandNotFoundException("Not found brand witd id: " + id);
        }
        return optBrand.get();
    }
    public void deleteBrand(Long id) throws BrandNotFoundException {
        Optional<Brand> optBrand = brandRepository.findById(id);
        if(optBrand.isEmpty()) {
            throw new BrandNotFoundException("Not found brand with id: "+ id);
        }
        brandRepository.deleteById(id);
    }

    public Boolean checkUniqueName(Long id, String name) {
        boolean isNewBrand = (id == null && id == 0);
        Brand brand = brandRepository.findByName(name);
        boolean result = false;
        if(isNewBrand) {
            if(brand != null) {
                result = true;
            }
        }else {
            if(brand != null && brand.getId() != id) {
                result = true;
            }
        }
        return result;
    }

    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    public Brand getBrandByIdRest(Long id) throws BrandNotFoundRestException {
        return brandRepository.findById(id).orElseThrow(
                () -> new BrandNotFoundRestException()
        );
    }

}
