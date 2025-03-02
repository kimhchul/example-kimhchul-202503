package com.example.productapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "카테고리 정보 응답 객체")
@Data
public class ResponseCtgDto implements Comparable<ResponseCtgDto> {

	@Schema(description = "카테고리명(컬럼값)")
	String ctg;

	@Schema(description = "카테고리명(노출값)")
	String ctgName;

	@Schema(description = "브랜드명")
	String brand;

	@Schema(description = "가격")
	Integer price;

	@Schema(description = "카테고리정렬순서", hidden = true)
	Integer sortOrder;

	public ResponseCtgDto() {
	}

	public ResponseCtgDto(String ctg, String ctgName, Integer sortOrder) {
		this.ctg = ctg;
		this.ctgName = ctgName;
		this.sortOrder = sortOrder;
	}

	@Override
	public int compareTo(ResponseCtgDto c) {
		return sortOrder - c.sortOrder;
	}
}
