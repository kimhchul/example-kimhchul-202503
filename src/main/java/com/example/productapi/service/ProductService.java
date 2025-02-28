package com.example.productapi.service;

import com.example.productapi.dto.ResponseCtgInfoDto;
import com.example.productapi.dto.ResponseLowPriceByCtgDto;
import com.example.productapi.dto.ResponseLowPriceByNameDto;
import com.example.productapi.dto.ResponseCtgDto;
import com.example.productapi.entity.Product;
import com.example.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	// 1. 컬럼별 최저가격과 해당 Name 조회 및 총합 계산
	public ResponseLowPriceByCtgDto getLowPriceByCtg() {


		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 각 카테고리별 최저가격을 구하고 총합을 구함
		 * 3. 카테고리별 최저가격의 브랜드정보와 총합을 리턴함
		 */

		List<Product> products = productRepository.findAll();

		//Map<String, Object> result = new HashMap<>();
		ResponseLowPriceByCtgDto result = new ResponseLowPriceByCtgDto();

		List<ResponseCtgDto> resultCtgList = new ArrayList<ResponseCtgDto>();
		result.setCtgList(resultCtgList);	//없으면 Empty

		int totalSum = 0;

		HashMap<String, String> ctgMappingMap = new HashMap<String, String>();
		ctgMappingMap.put("ctgTops","상의");
		ctgMappingMap.put("ctgOuterwear","아우터");
		ctgMappingMap.put("ctgPants","바지");
		ctgMappingMap.put("ctgSneakers","스니커즈");
		ctgMappingMap.put("ctgBag","가방");
		ctgMappingMap.put("ctgHat","모자");
		ctgMappingMap.put("ctgSocks","양말");
		ctgMappingMap.put("ctgAccessories","악세서리");

		for(String key : ctgMappingMap.keySet())
		{
			String ctgField = key;
			int minPrice = Integer.MAX_VALUE;
			String minBrand = "";

			for (Product p : products) {
				int price = getCtgValue(p, ctgField);
				if (price < minPrice) {
					minPrice = price;
					minBrand = p.getBrand();
				}
			}

			totalSum += minPrice;
			ResponseCtgDto itemDto = new ResponseCtgDto();
			itemDto.setCtg(ctgField);
			itemDto.setBrand(minBrand);
			itemDto.setPrice(minPrice);
			resultCtgList.add(itemDto);
		}

		result.setTotalSum(totalSum);
		result.setDescription("카테고리별 최저가 항목 정보 & 카테고리별 최저가 항목의 합");

		return result;
	}

	// 2. Name별 8개 컬럼 총합이 가장 작은 상품 조회
	public ResponseLowPriceByNameDto getLowPriceByName() {

		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 각 브랜드별 카테고리 합산 가격을 구함
		 * 3. 최소값을 가지는 브랜드의 정보를 리턴함
		 */
		ResponseLowPriceByNameDto result = new ResponseLowPriceByNameDto();


		List<Product> products = productRepository.findAll();


		Product minProduct = null;
		Integer minTotal = Integer.MAX_VALUE;

		for (Product p : products) {
			Integer total = p.getCtgTops() + p.getCtgOuterwear() + p.getCtgPants() + p.getCtgSneakers()
					+ p.getCtgBag() + p.getCtgHat() + p.getCtgSocks() + p.getCtgAccessories();
			if (total < minTotal) {
				minTotal = total;
				minProduct = p;
			}
		}

		if (minProduct != null) {
			//?? 변수명확인
			result.setCtg(minProduct.getBrand());
			result.setTotalSum(minTotal);
			result.setProduct(minProduct);
			result.setDescription("항목의 총합이 가장 작은 항목의 정보");

		}
		else
		{

			result.setDescription("조회실패.");
		}

		return result;
	}

	// 3. 특정 컬럼에서 최고가격의 상품 조회
	public ResponseCtgInfoDto getCtgInfo(String ctg) {

		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 대상 카테고리의 최저가격을 구함
		 * 3. 최소값을 가지는 브랜드의 정보를 리턴함
		 */


		List<Product> products = productRepository.findAll();

		ResponseCtgInfoDto result = new ResponseCtgInfoDto();

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
			int price = getCtgValue(p, ctg);
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
			result.setMinProduct(minProduct);
		}
		if (maxProduct != null) {
			result.setMaxProduct(maxProduct);
		}

		if (minProduct == null || maxProduct == null)
		{
			result.setDescription("정상조회 실패");
		}
		else {
			result.setDescription("카테고리기준 브랜드정보");
		}

		return result;
	}

	// 4. Name을 키로 Row 추가/변경/삭제
	public String changeInfo(String type, Product product) {
		switch (type.toLowerCase()) {
			case "insert":
				if (productRepository.existsById(product.getBrand())) {
					return "이미 존재하는 Brand 입니다.";
				}
				productRepository.save(product);
				return "추가되었습니다.";
			case "update":

				Optional<Product> productToUpdate = productRepository.findById(product.getBrand());

				if (productToUpdate.isEmpty()) {
					return "존재하지 않는 Brand 입니다.";
				}

				Product existingProduct = productToUpdate.get();

				if(product.getCtgTops() != null) {
					existingProduct.setCtgTops(product.getCtgTops());
				}
				if(product.getCtgOuterwear() != null) {
					existingProduct.setCtgOuterwear(product.getCtgOuterwear());
				}
				if(product.getCtgPants() != null) {
					existingProduct.setCtgPants(product.getCtgPants());
				}
				if(product.getCtgSneakers() != null) {
					existingProduct.setCtgSneakers(product.getCtgSneakers());
				}
				if(product.getCtgBag() != null) {
					existingProduct.setCtgBag(product.getCtgBag());
				}
				if(product.getCtgHat() != null) {
					existingProduct.setCtgHat(product.getCtgHat());
				}
				if(product.getCtgSocks() != null) {
					existingProduct.setCtgSocks(product.getCtgSocks());
				}
				if(product.getCtgAccessories() != null) {
					existingProduct.setCtgAccessories(product.getCtgAccessories());
				}


				productRepository.save(existingProduct);
				return "변경되었습니다.";
			case "delete":
				if (!productRepository.existsById(product.getBrand())) {
					return "존재하지 않는 Name입니다.";
				}
				productRepository.deleteById(product.getBrand());
				return "삭제되었습니다.";
			default:
				return "유효하지 않은 타입입니다. (Insert, Update, Delete 중 선택)";
		}
	}

	// 헬퍼 메서드: 동적으로 ctg 값 가져오기
	private int getCtgValue(Product product, String ctgField) {
		switch (ctgField) {
			case "ctgTops": return product.getCtgTops();
			case "ctgOuterwear": return product.getCtgOuterwear();
			case "ctgPants": return product.getCtgPants();
			case "ctgSneakers": return product.getCtgSneakers();
			case "ctgBag": return product.getCtgBag();
			case "ctgHat": return product.getCtgHat();
			case "ctgSocks": return product.getCtgSocks();
			case "ctgAccessories": return product.getCtgAccessories();
			default: return 0;
		}
	}
}
