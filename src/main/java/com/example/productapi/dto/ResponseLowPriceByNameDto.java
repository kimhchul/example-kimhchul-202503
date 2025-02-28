package com.example.productapi.dto;


import com.example.productapi.entity.Product;
import lombok.Data;

@Data
public class ResponseLowPriceByNameDto {

	Integer totalSum;

	String ctg;

	Product product;

	String description;

}

