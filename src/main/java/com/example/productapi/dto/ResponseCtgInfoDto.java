package com.example.productapi.dto;


import com.example.productapi.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "특정 컬럼에서 최저/최고가격 정보 조회 응답 객체")
public class ResponseCtgInfoDto {

	@Schema(description = "최저가격 정보 Product", example="abcd")
	Product minProduct;

	@Schema(description = "최고가격 정보 Product", example="abcd")
	Product maxProduct;

	@Schema(description = "응답메세지설명", example="abcd")
	String description;

}

