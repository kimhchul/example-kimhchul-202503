package com.example.productapi.controller;

import com.example.productapi.dto.ResponseCtgInfoDto;
import com.example.productapi.dto.ResponseLowPriceByCtgDto;
import com.example.productapi.dto.ResponseLowPriceByNameDto;
import com.example.productapi.entity.Product;
import com.example.productapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	// 1. 컬럼별 최저가격과 해당 Name 조회 및 총합 응답
	@GetMapping("/query/getLowPriceByCtg")
	public ResponseEntity<ResponseLowPriceByCtgDto> getLowPriceByCtg() {
		ResponseLowPriceByCtgDto result = productService.getLowPriceByCtg();
		return ResponseEntity.ok(result);
	}

	// 2. Name별 8개 컬럼 총합이 가장 작은 상품 정보 조회
	@GetMapping("/query/getLowPriceByName")
	public ResponseEntity<ResponseLowPriceByNameDto> getLowPriceByName() {
		ResponseLowPriceByNameDto result = productService.getLowPriceByName();
		return ResponseEntity.ok(result);
	}

	// 3. 컬럼명으로 최고가격의 Row 정보 조회
	@GetMapping("/query/getCtgInfo")
	public ResponseEntity<ResponseCtgInfoDto> getCtgInfo(@RequestParam String ctgName) {
		ResponseCtgInfoDto result = productService.getCtgInfo(ctgName);
		return ResponseEntity.ok(result);
	}

	// 4. Name을 키로 Row 추가/변경/삭제
	@PostMapping("/command/changeInfo")
	public ResponseEntity<String> changeInfo(@RequestParam String type, @RequestBody Product product) {
		String message = productService.changeInfo(type, product);
		return ResponseEntity.ok(message);
	}
}
