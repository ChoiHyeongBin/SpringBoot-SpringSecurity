package com.chb.security1.model;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data		// Getter, Setter 생성
public class User {
	@Id		// primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;		// ROLE_USER, ROLE_ADMIN
	
	private String provider;
	private String providerId;
	@CreationTimestamp		// 자동으로 만들어줌
	private Timestamp createDate;
}
