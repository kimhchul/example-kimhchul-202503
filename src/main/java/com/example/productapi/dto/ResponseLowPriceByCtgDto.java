package com.example.productapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "카테고리 별 최저가격 브랜드와 상품 가격, 총액 응답 객체")
@Data
public class ResponseLowPriceByCtgDto {

	@Schema(description = "카테고리 별 결과정보 리스트")
	List<ResponseCtgDto> ctgList;

	@Schema(description = "총합")
	Integer totalSum;

	@Schema(description = "응답메세지")
	String resMsg;

}

