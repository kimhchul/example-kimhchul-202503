package com.example.productapi.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {
	//브랜드
	@Id
	private String brand;

	//상의
	private Integer ctgTops;

	//아우터
	private Integer ctgOuterwear;

	//바지
	private Integer ctgPants;

	//스니커즈
	private Integer ctgSneakers;

	//가방
	private Integer ctgBag;

	//모자
	private Integer ctgHat;

	//양말
	private Integer ctgSocks;

	//악세서리
	private Integer ctgAccessories;

	public Product() {
	}

	// 생성자
	public Product(String brand, Integer ctgTops, Integer ctgOuterwear, Integer ctgPants
			, Integer ctgSneakers, Integer ctgBag, Integer ctgHat, Integer ctgSocks, Integer ctgAccessories) {
		this.brand = brand;
		this.ctgTops = ctgTops;
		this.ctgOuterwear = ctgOuterwear;
		this.ctgPants = ctgPants;
		this.ctgSneakers = ctgSneakers;
		this.ctgBag = ctgBag;
		this.ctgHat = ctgHat;
		this.ctgSocks = ctgSocks;
		this.ctgAccessories = ctgAccessories;
	}

}

