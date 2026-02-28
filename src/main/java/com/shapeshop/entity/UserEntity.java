package com.shapeshop.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import com.shapeshop.model.UserRole;
@Getter
@Setter
//@Data
@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(nullable = false, unique = true)
	private String userName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(nullable = false)
	private String password;

	public UserEntity() {
		super();
	}

	public UserEntity(UserRole role, String userName, String password) {
		super();
		this.role = role;
		this.userName = userName;
		this.password = password;
	}
}
