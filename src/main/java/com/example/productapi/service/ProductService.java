package com.example.productapi.service;

import com.example.productapi.entity.Product;
import com.example.productapi.repository.ProductRepository;
import org.hibernate.jpa.internal.util.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	// 1. 컬럼별 최저가격과 해당 Name 조회 및 총합 계산
	public Map<String, Object> getLowPriceByCtg() {


		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 각 카테고리별 최저가격을 구하고 총합을 구함
		 * 3. 카테고리별 최저가격의 브랜드정보와 총합을 리턴함
		 */

		List<Product> products = productRepository.findAll();

		Map<String, Object> result = new HashMap<>();
		int totalSum = 0;

		for (int i = 1; i <= 8; i++) {
			String ctgField = "ctg" + i;
			int minPrice = Integer.MAX_VALUE;
			String minName = "";

			for (Product p : products) {
				int price = getCtgValue(p, ctgField);
				if (price < minPrice) {
					minPrice = price;
					minName = p.getName();
				}
			}

			totalSum += minPrice;
			result.put(ctgField, Map.of("name", minName, "price", minPrice));
		}

		result.put("totalSum", totalSum);
		result.put("description", "카테고리별 최저가 항목 정보 & 카테고리별 최저가 항목의 합");
		return result;
	}

	// 2. Name별 8개 컬럼 총합이 가장 작은 상품 조회
	public Map<String, Object> getLowPriceByName() {

		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 각 브랜드별 카테고리 합산 가격을 구함
		 * 3. 최소값을 가지는 브랜드의 정보를 리턴함
		 */

		List<Product> products = productRepository.findAll();


		Product minProduct = null;
		int minTotal = Integer.MAX_VALUE;

		for (Product p : products) {
			int total = p.getCtg1() + p.getCtg2() + p.getCtg3() + p.getCtg4()
					+ p.getCtg5() + p.getCtg6() + p.getCtg7() + p.getCtg8();
			if (total < minTotal) {
				minTotal = total;
				minProduct = p;
			}
		}

		if (minProduct != null) {
			Map<String, Object> result = new HashMap<>();
			result.put("name", minProduct.getName());
			result.put("total", minTotal);
			result.put("details", minProduct);
			result.put("description", "항목의 총합이 가장 작은 항목의 정보");
			return result;
		}

		return null;
	}

	// 3. 특정 컬럼에서 최고가격의 상품 조회
	public Map<String, Object>  getCtgInfo(String ctgName) {

		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 대상 카테고리의 최저가격을 구함
		 * 3. 최소값을 가지는 브랜드의 정보를 리턴함
		 */


		List<Product> products = productRepository.findAll();

		Map<String, Object> result = new HashMap<>();

		Product minProduct = null;
		Product maxProduct = null;

		int minPrice = Integer.MAX_VALUE;
		int maxPrice = Integer.MIN_VALUE;


		/**
		 * 최저/최고 가격이 같은상품이 존재할수있음
		 * 해당로직에서는 최고/최저 가격 대상으로 판단된 첫번째 Product를 대상으로 선정함
		 * 	별도의 우선순위가 있다면 아래 min/max 선정 로직에서 추가제어가능함
		 * 	만약, 동일 최저가를 모두 리스트업해야한다면 min/max 기준을 list 형태로 변경해야함
		 */

		for (Product p : products) {
			int price = getCtgValue(p, ctgName);
			if (price > maxPrice) {
				maxPrice = price;
				maxProduct = p;
			}
			else if(price < minPrice)
			{
				minPrice = price;
				minProduct = p;
			}
		}


		if (minProduct != null) {
			result.put("minProduct", minProduct);
		}
		if (maxProduct != null) {
			result.put("maxproductinfo", maxProduct);
		}


		return result;
	}

	// 4. Name을 키로 Row 추가/변경/삭제
	public String changeInfo(String type, Product product) {
		switch (type.toLowerCase()) {
			case "insert":
				if (productRepository.existsById(product.getName())) {
					return "이미 존재하는 Name입니다.";
				}
				productRepository.save(product);
				return "추가되었습니다.";
			case "update":

				Optional<Product> productToUpdate = productRepository.findById(product.getName());

				if (productToUpdate.isEmpty()) {
					return "존재하지 않는 Name입니다.";
				}

				Product existingProduct = productToUpdate.get();
				// 기존 데이터와 새로운 데이터 병합
				if(product.getCtg1() != null) {
					existingProduct.setCtg1(product.getCtg1());
				}
				if(product.getCtg2() != null) {
					existingProduct.setCtg2(product.getCtg2());
				}
				if(product.getCtg3() != null) {
					existingProduct.setCtg3(product.getCtg3());
				}
				if(product.getCtg4() != null) {
					existingProduct.setCtg4(product.getCtg4());
				}
				if(product.getCtg5() != null) {
					existingProduct.setCtg5(product.getCtg5());
				}
				if(product.getCtg6() != null) {
					existingProduct.setCtg6(product.getCtg6());
				}
				if(product.getCtg7() != null) {
					existingProduct.setCtg7(product.getCtg7());
				}
				if(product.getCtg8() != null) {
					existingProduct.setCtg8(product.getCtg8());
				}

				// 나머지 필드도 동일하게 설정
				productRepository.save(existingProduct);
				return "변경되었습니다.";
			case "delete":
				if (!productRepository.existsById(product.getName())) {
					return "존재하지 않는 Name입니다.";
				}
				productRepository.deleteById(product.getName());
				return "삭제되었습니다.";
			default:
				return "유효하지 않은 타입입니다. (Insert, Update, Delete 중 선택)";
		}
	}

	// 헬퍼 메서드: 동적으로 ctg 값 가져오기
	private int getCtgValue(Product product, String ctgField) {
		switch (ctgField.toLowerCase()) {
			case "ctg1": return product.getCtg1();
			case "ctg2": return product.getCtg2();
			case "ctg3": return product.getCtg3();
			case "ctg4": return product.getCtg4();
			case "ctg5": return product.getCtg5();
			case "ctg6": return product.getCtg6();
			case "ctg7": return product.getCtg7();
			case "ctg8": return product.getCtg8();
			default: return 0;
		}
	}
}
