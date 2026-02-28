package com.shapeshop.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "product_category")
public class ProductCategoryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private ProductEntity product = new ProductEntity();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, updatable = false)
    private CategoryEntity category = new CategoryEntity();

    public ProductCategoryEntity() {
    }
    public ProductCategoryEntity(CategoryEntity category, ProductEntity product) {
        this.category = category;
        this.product = product;
    }
}
