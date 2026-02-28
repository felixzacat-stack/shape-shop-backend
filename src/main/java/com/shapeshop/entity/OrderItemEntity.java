package com.shapeshop.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "orders_item")
public class OrderItemEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false, updatable = false)
	private ProductEntity product = new ProductEntity();

//	@ManyToOne(optional = false, fetch = FetchType.EAGER)
//	@JoinColumn(name = "order_id", nullable = false, updatable = false)
//	private OrderEntity order;

	@Column(name = "order_amount")
	private int amount;

	protected OrderItemEntity() {
	}

	public OrderItemEntity(ProductEntity product, int amount) {
		this.product = product;
		this.amount = amount;
	}
}
