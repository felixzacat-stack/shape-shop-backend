package com.shapeshop.service;

import com.shapeshop.ShapeShopException;
import com.shapeshop.entity.CategoryEntity;
import com.shapeshop.entity.ProductCategoryEntity;
import com.shapeshop.entity.CompanyEntity;
import com.shapeshop.entity.ProductEntity;
import com.shapeshop.entity.dto.CategoryDto;
import com.shapeshop.entity.dto.Converter;
import com.shapeshop.repository.CategoryRepository;
import com.shapeshop.repository.CompanyRepository;
import com.shapeshop.repository.ProductCategoryRepository;
import com.shapeshop.repository.ProductRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    EntityManager entitymanager;

    @Autowired
    CategoryRepository catRep;

    @Autowired
    ProductCategoryRepository prodCatRep;

    @Autowired
    ProductRepository prodRep;

    @Autowired
    private CompanyRepository companyRep;

    public CategoryEntity createCategory(CategoryEntity categoryEntity, String companyName) throws ShapeShopException {

        CompanyEntity company = companyRep.findByName(companyName);
        if (company == null) {
            throw new ShapeShopException("Company does not exist ", ShapeShopException.ErrorType.COMPANY_DOES_NOT_EXIST);
        }
        categoryEntity.setCompany(company);
        categoryEntity.setId(0);

        categoryEntity = catRep.save(categoryEntity);
        return categoryEntity;
    }

    public void deleteCategory(long id, String companyName) throws ShapeShopException {

        var cat = catRep.findById(id);

        prodCatRep.deleteAll(cat.getProductCategory());

        catRep.deleteById(id);
    }

    public CategoryEntity updateCategory(CategoryEntity categoryEntity, String companyName, Long id) throws ShapeShopException {

        CompanyEntity company = companyRep.findByName(companyName);

        List<CategoryEntity> categoryEntities = catRep.findByCompany(company);

        val foundEntity = categoryEntities.stream().filter(e -> e.getId() == id).findFirst();

        if (foundEntity.isPresent()) {
            foundEntity.get().setName(categoryEntity.getName());
            return catRep.save(foundEntity.get());
        }else {
            throw new ShapeShopException("Category does not exist " + categoryEntity.getName(), ShapeShopException.ErrorType.PROD_NOT_FOUND);
        }
    }

    public ProductCategoryEntity getCategory(ProductCategoryEntity categoryEntity, String companyName) throws ShapeShopException {

        CompanyEntity company = companyRep.findByName(companyName);
        if (company == null) {
            throw new ShapeShopException("Company does not exist ", ShapeShopException.ErrorType.COMPANY_DOES_NOT_EXIST);
        }
        return categoryEntity;
    }
}
