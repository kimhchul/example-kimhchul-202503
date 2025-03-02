package com.example.productapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Schema(description = "단일 브랜드로 모든 상품구매시, 최저가격 판매 브랜드와 카테고리 가격및 총액 응답 객체")
@Data
public class ResponseLowPriceByBrandDto {


	@Schema(description = "브랜드명")
	String brand;

	@Schema(description = "카테고리 별 결과정보 리스트")
	List<ResponseCtgDto> ctgList;

	@Schema(description = "총합")
	Integer totalSum;

	@Schema(description = "응답메세지")
	String resMsg;

}

