package com.example.productapi.dto;


import lombok.Data;

import java.util.List;

@Data
public class ResponseLowPriceByCtgDto {

	Integer totalSum;

	List<ResponseCtgDto> ctgList;

	String description;

}

