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
@Table(name = "company")
public class CompanyEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "name")
	private String name;

	protected CompanyEntity() {}

	public CompanyEntity(String name) {
		this.name = name;
	}
}