package com.shapeshop.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false, updatable = false)
	private CompanyEntity company = new CompanyEntity();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private List<OrderItemEntity> orders = new ArrayList<>(); //todo rename ordersItems

	@OneToMany
	@JoinColumn(name = "product_id")
	private List<ProductCategoryEntity> productCategories = new ArrayList<>();

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "sash_text")
	private String sashText;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "img_filename")
	private String imageFilename;

	protected ProductEntity() {
	}

	public ProductEntity(String name, CompanyEntity company) {
		this.name = name;
		this.company = company;
	}

	public ProductEntity(String name, BigDecimal price, String imageFilename, String description, String sashText) {
		this.name = name;
		this.price = price;
		this.sashText = sashText;
		this.imageFilename = imageFilename;
		this.description = description;
	}

//	public ProductEntity(String name, BigDecimal price, String imageFilename, CompanyEntity company, String description) {
//		this(name, price, imageFilename, company);
//		this.description = description;
	//}
//	public ProductEntity(String name, BigDecimal price, String imageFilename, CompanyEntity company) {
//		this.name = name;
//		this.price = price;
//		this.imageFilename = imageFilename;
//		this.company = company;
//	}
}
