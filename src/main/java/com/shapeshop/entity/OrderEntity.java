package com.shapeshop.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Getter
@Setter

@Entity

//TODO remove plurality. cannot rename to "order" because reserved word in SQL
@Table(name = "orders")
public class OrderEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private CompanyEntity company = new CompanyEntity();

	@OneToOne
	private CreditCardEntity creditCardEntity;

	@OneToOne
	private AddressEntity addressEntity;

	@OneToMany
	@JoinColumn(name = "order_id")
	private List<OrderItemEntity> orderItems;

//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "product_id")

	@Column(name = "date")
	private Date date;
	@Column(name = "payment_type")
	private PaymentType paymentType;
	@Column(name = "delivery_type")
	private DeliveryType deliveryType;
	@Column(name = "state")
	private OrderState state = OrderState.OPEN;

	protected OrderEntity() {
	}

	public OrderEntity(Date date, PaymentType payment, DeliveryType deliveryType, AddressEntity address,
			CreditCardEntity creditCard, CompanyEntity company) {
		this.addressEntity = address;
		this.creditCardEntity = creditCard;
		this.date = date;
		this.paymentType = payment;
		this.deliveryType = deliveryType;
		this.company = company;
	}

//	public void addOrderItem(OrderItemEntity item) {
//		orderItems.add(item);
//	}
}
