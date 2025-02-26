package com.example.productapi.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {

	@Id
	private String name;

	private Integer ctg1;
	private Integer ctg2;
	private Integer ctg3;
	private Integer ctg4;
	private Integer ctg5;
	private Integer ctg6;
	private Integer ctg7;
	private Integer ctg8;

	// 기본 생성자
	public Product() {
	}

	// 생성자
	public Product(String name, Integer ctg1, Integer ctg2, Integer ctg3
			, Integer ctg4, Integer ctg5, Integer ctg6, Integer ctg7, Integer ctg8) {
		this.name = name;
		this.ctg1 = ctg1;
		this.ctg2 = ctg2;
		this.ctg3 = ctg3;
		this.ctg4 = ctg4;
		this.ctg5 = ctg5;
		this.ctg6 = ctg6;
		this.ctg7 = ctg7;
		this.ctg8 = ctg8;
	}
}

