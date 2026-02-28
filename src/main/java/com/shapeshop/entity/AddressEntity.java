package com.shapeshop.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * TODO rename to Contact data (Does not necessarily need to correspond to a
 * whole address)
 */
@Getter
@Setter
@Entity
@Table(name = "address")
public class AddressEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String street;
	private String postcode;
	private String telephone;
	private String email;

	protected AddressEntity() {
	}

	public AddressEntity(String name, String street, String postcode, String telephone, String email) {
		this.name = name;
		this.street = street;
		this.postcode = postcode;
		this.telephone = telephone;
		this.email = email;
	}
}
