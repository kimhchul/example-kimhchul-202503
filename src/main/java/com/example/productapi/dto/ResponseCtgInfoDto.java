package com.example.productapi.dto;


import com.example.productapi.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "특정 컬럼에서 최저/최고가격 정보 조회 응답 객체")
public class ResponseCtgInfoDto {

	@Schema(description = "카테고리명(컬럼값)")
	String ctg;

	@Schema(description = "카테고리명(노출값)")
	String ctgName;


	@Schema(description = "최저가격 정보 ResponseCtgDto")
	ResponseCtgDto minCtgDto;

	@Schema(description = "최고가격 정보 ResponseCtgDto")
	ResponseCtgDto maxCtgDto;

	@Schema(description = "응답메세지")
	String resMsg;

}

