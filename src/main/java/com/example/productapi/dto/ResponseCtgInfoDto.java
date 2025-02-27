package com.example.productapi.dto;


import com.example.productapi.entity.Product;
import lombok.Data;

@Data
public class ResponseCtgInfoDto {

	Product minProduct;
	Product maxProduct;

	String description;

}

