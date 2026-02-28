package com.shapeshop.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, updatable = false)
    private CompanyEntity company = new CompanyEntity();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private List<ProductCategoryEntity> productCategory;

    @Column(name = "name")
    private String name;

    protected CategoryEntity() {
    }

    public CategoryEntity(String name, CompanyEntity company) {
        this.name = name;
        this.company = company;
    }

    public CategoryEntity(String name) {
        this.name = name;
    }

    public String toString() {
        return "testing testing";
    }

}
