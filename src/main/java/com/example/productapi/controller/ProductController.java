package com.example.productapi.controller;

import com.example.productapi.dto.ResponseCtgInfoDto;
import com.example.productapi.dto.ResponseLowPriceByCtgDto;
import com.example.productapi.dto.ResponseLowPriceByBrandDto;
import com.example.productapi.entity.Product;
import com.example.productapi.service.ProductService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@Operation(summary = "구현1 - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API", description = "카테고리별 최저가격과 해당 브랜드를을 조회하고, 총합을 반환합니다.")
	@ApiResponse(responseCode = "200", description="success")
	@GetMapping("/query/getLowPriceByCtg")
	public ResponseEntity<ResponseLowPriceByCtgDto> getLowPriceByCtg() {
		ResponseLowPriceByCtgDto result = productService.getLowPriceByCtg();
		return ResponseEntity.ok(result);
	}

	// 2.총합이 가장 작은 브랜드 조회
	@Operation(summary = "구현2 - 단일 브랜드로 모든 상품구매시, 최저가격 판매 브랜드와 카테고리 가격및 총액 조회 API", description = "8개 컬럼의 총합이 가장 작은 상품 정보를 조회합니다.")
	@GetMapping("/query/getLowPriceByBrand")
	public ResponseEntity<ResponseLowPriceByBrandDto> getLowPriceByBrand() {
		ResponseLowPriceByBrandDto result = productService.getLowPriceByBrand();
		return ResponseEntity.ok(result);
	}

	// 3. 특정 카테고리의 최저/최고가격의 브랜드 조회
	@Operation(summary = "구현3 - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가겨을 조회하는 API", description = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가겨을 조회합니다.")
	@GetMapping("/query/getCtgInfo")

	public ResponseEntity<ResponseCtgInfoDto> getCtgInfo(@RequestParam(required = true, defaultValue = "상의") String ctgName) {
		ResponseCtgInfoDto result = productService.getCtgInfo(ctgName);
		return ResponseEntity.ok(result);
	}

	// 4. 브랜드 정보 추가/변경/삭제
	@Operation(summary = "구현4 - 브랜드 및 상품을 추가/변경/삭제", description = "브랜드를 key로 브랜드 정보를 추가(type=insert) 변경(type=update), 삭제(type=delete)합니다.")
	@PostMapping("/command/changeInfo")
	public ResponseEntity<String> changeInfo(@RequestParam(required = true, defaultValue = "update") String type
			, @RequestBody(required = true) Product product) {
		String message = productService.changeInfo(type, product);
		return ResponseEntity.ok(message);
	}
}
