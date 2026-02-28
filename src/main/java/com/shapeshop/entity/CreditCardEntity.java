package com.shapeshop.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "credit_card")
public class CreditCardEntity {

	@Id
	@Column(name = "cc_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "number")
	private String number;
	@Column(name = "exp_date")
	private String expDate;
	@Column(name = "name")
	private String name;
	@Column(name = "type")
	private String type;

	protected CreditCardEntity() {
	}

	public CreditCardEntity(String number, String expDate, String name, String type) {
		this.number = number;
		this.expDate = expDate;
		this.name = name;
		this.type = type;
	}
}
