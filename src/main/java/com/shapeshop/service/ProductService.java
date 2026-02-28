package com.shapeshop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.shapeshop.ShapeShopException;
import com.shapeshop.entity.*;
import com.shapeshop.repository.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class ProductService {

    @Autowired
    ProductCategoryRepository prodCatRep;

    @Autowired
    EntityManager entitymanager;

    @Autowired
    ProductRepository productRep;

    @Autowired
    OrderRepository orderRep;

    @Autowired
    ProductCategoryRepository pcRep;

    @Autowired
    private CompanyRepository companyRep;

    @Autowired
    private CategoryRepository categoryRep;

    public ProductEntity createProduct(ProductEntity product, String companyName, String categoryName) throws ShapeShopException {

        CompanyEntity company = companyRep.findByName(companyName);
        if (company == null) {
            throw new ShapeShopException("Company does not exist : " + companyName, ShapeShopException.ErrorType.COMPANY_DOES_NOT_EXIST);
        }
        product.setCompany(company);

        List<CategoryEntity> l = categoryRep.findByCompany(company);
        CategoryEntity cat = null;
        for (CategoryEntity categoryEntity : l) {
            if (categoryEntity.getName().equals(categoryName)) {
                cat = categoryEntity;
            }
        }

        if (cat == null) {
            throw new ShapeShopException("Category does not exist : " + categoryName, ShapeShopException.ErrorType.CATEGORY_DOES_NOT_EXIST);
        }

        product = productRep.save(product);

        ArrayList<ProductCategoryEntity> al = new ArrayList<>();
        al.add(new ProductCategoryEntity(cat, product));
        prodCatRep.saveAll(al);

        return product;
    }

    /**
     * @deprecated can only create product with a cat
     */
    public ProductEntity createProduct(ProductEntity product, String companyName) throws ShapeShopException {

        CompanyEntity company = companyRep.findByName(companyName);
        if (company == null) {
            throw new ShapeShopException("Company does not exist ", ShapeShopException.ErrorType.COMPANY_DOES_NOT_EXIST);
        }
        product.setCompany(company);
        product.setId(0);

        product = productRep.save(product);
        return product;
    }

    public void updateProduct(ProductEntity product, Long id, String companyName) throws ShapeShopException {

        CompanyEntity company = companyRep.findByName(companyName);
        if (company == null) {
            throw new ShapeShopException("Company does not exist ", ShapeShopException.ErrorType.COMPANY_DOES_NOT_EXIST);
        }

        List<ProductEntity> prods = this.getProductsByCompany(company);

        val foundEntity = prods.stream().filter(e -> e.getId() == id).findFirst();

        if (foundEntity.isPresent()) {
            if (product.getName() != null)
                foundEntity.get().setName(product.getName());
            if (product.getPrice() != null)
                foundEntity.get().setPrice(product.getPrice());
            if (product.getSashText() != null)
                foundEntity.get().setSashText(product.getSashText());
            productRep.save(foundEntity.get());
        }else {
            throw new ShapeShopException("Product does not exist ", ShapeShopException.ErrorType.PROD_NOT_FOUND);
        }
    }

    public List<ProductEntity> getProductsByCompany(CompanyEntity company) {
        Query productsByCompany = entitymanager.
                createQuery("Select p from ProductEntity p where p.company.name = :compName").setParameter("compName", company.getName());

        List<ProductEntity> products = productsByCompany.getResultList();

        return products;
    }

    public List<ProductEntity> getProductsByCompanyAndCategory(CompanyEntity c, CategoryEntity cat) {

        Query productsByCompany = entitymanager.
                createQuery("Select p from ProductEntity p where p.company.name = :compName").setParameter("compName", c.getName());

        List<ProductEntity> products = productsByCompany.getResultList();

        List<ProductEntity> products2 = new ArrayList<>();

        for (ProductEntity product : products) {
            var prodCats = product.getProductCategories().stream().filter(pc -> pc.getCategory().getId() == cat.getId()).collect(Collectors.toList());
            if (prodCats.size() > 0)
                products2.add(product);
        }

        return products2;
    }

    public List<ProductEntity> getAllProducts() {
        List<ProductEntity> result = StreamSupport.stream(productRep.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return result;
    }

    public ProductEntity getProductById(long id) {
        return productRep.findById(id);
    }

    public void deleteProduct(long id) {

        ProductEntity prod = productRep.findById(id);

        prodCatRep.deleteAll(prod.getProductCategories());

        productRep.deleteById(id);
    }

    public void deleteProduct(ProductEntity p) {
        productRep.delete(p);
    }
}
