package com.example.productapi.dto;


import com.example.productapi.entity.Product;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//@ApiModel(description = "특정 컬럼에서 최저/최고가격 정보 조회 응답 객체")
@Data
public class ResponseCtgInfoDto {

	//@ApiModelProperty(value = "최저가격 정보", example = "{}")
	Product minProduct;

	//@ApiModelProperty(value = "최고가격 정보", example = "{}")
	Product maxProduct;

	String description;

}

