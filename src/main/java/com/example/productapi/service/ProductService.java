package com.example.productapi.service;

import com.example.productapi.dto.ResponseCtgInfoDto;
import com.example.productapi.dto.ResponseLowPriceByCtgDto;
import com.example.productapi.dto.ResponseLowPriceByBrandDto;
import com.example.productapi.dto.ResponseCtgDto;
import com.example.productapi.entity.Product;
import com.example.productapi.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * 카테고리명 매핑 (컬러명 - 카테고리명)
	 */
	public final static Map<String, ResponseCtgDto> ctgMappingMap = new HashMap<String, ResponseCtgDto>() {
		{
			put("ctgTops", new ResponseCtgDto("ctgTops","상의",1));
			put("ctgOuterwear", new ResponseCtgDto("ctgOuterwear","아우터",2));
			put("ctgPants", new ResponseCtgDto("ctgPants","바지",3));
			put("ctgSneakers", new ResponseCtgDto("ctgSneakers","스니커즈",4));
			put("ctgBag", new ResponseCtgDto("ctgBag","가방",5));
			put("ctgHat", new ResponseCtgDto("ctgHat","모자",6));
			put("ctgSocks", new ResponseCtgDto("ctgSocks","양말",7));
			put("ctgAccessories", new ResponseCtgDto("ctgAccessories","악세서리",8));
		}
	};

	// 카테고리별 최저가격과 해당 브랜드를을 조회하고, 총합을 반환합니다.
	public ResponseLowPriceByCtgDto getLowPriceByCtg() {


		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 각 카테고리별 최저가격을 구하고 총합을 구함
		 * 3. 카테고리별 최저가격의 브랜드정보와 총합을 리턴함
		 */

		List<Product> products = productRepository.findAll();

		ResponseLowPriceByCtgDto result = new ResponseLowPriceByCtgDto();

		List<ResponseCtgDto> resultCtgList = new ArrayList<ResponseCtgDto>();
		result.setCtgList(resultCtgList);	//없으면 Empty

		int totalSum = 0;


		for(String key : ctgMappingMap.keySet())
		{
			String ctg = key;
			int minPrice = Integer.MAX_VALUE;
			String minBrand = "";

			for (Product p : products) {
				int price = getCtgValue(ctg, p);
				if (price < minPrice) {
					minPrice = price;
					minBrand = p.getBrand();
				}
			}

			totalSum += minPrice;
			ResponseCtgDto resCtgDto = new ResponseCtgDto();
			BeanUtils.copyProperties(ctgMappingMap.get(ctg), resCtgDto);

			resCtgDto.setBrand(minBrand);
			resCtgDto.setPrice(minPrice);
			resultCtgList.add(resCtgDto);
		}
		//sort order 기준 정렬
		Collections.sort(resultCtgList);
		result.setTotalSum(totalSum);
		result.setResMsg("카테고리별 최저가 항목 정보 & 카테고리별 최저가 항목의 합");

		return result;
	}

	// 8개 컬럼의 총합이 가장 작은 상품 정보를 조회합니다.
	public ResponseLowPriceByBrandDto getLowPriceByBrand() {

		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 각 브랜드별 카테고리 합산 가격을 구함
		 * 3. 최소값을 가지는 브랜드의 정보를 리턴함
		 */
		ResponseLowPriceByBrandDto result = new ResponseLowPriceByBrandDto();


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
			List<ResponseCtgDto> resultCtgList = new ArrayList<ResponseCtgDto>();

			resultCtgList.add(setCtgDto("ctgTops", minProduct));
			resultCtgList.add(setCtgDto("ctgOuterwear", minProduct));
			resultCtgList.add(setCtgDto("ctgPants", minProduct));
			resultCtgList.add(setCtgDto("ctgSneakers", minProduct));
			resultCtgList.add(setCtgDto("ctgBag", minProduct));
			resultCtgList.add(setCtgDto("ctgHat", minProduct));
			resultCtgList.add(setCtgDto("ctgSocks", minProduct));
			resultCtgList.add(setCtgDto("ctgAccessories", minProduct));

			result.setBrand(minProduct.getBrand());

			result.setCtgList(resultCtgList);

			result.setTotalSum(minTotal);

			result.setResMsg("항목의 총합이 가장 작은 항목의 정보");

		}
		else
		{

			result.setResMsg("조회실패.");
		}

		return result;
	}

	// 3. 특정 컬럼에서 최고가격의 상품 조회
	public ResponseCtgInfoDto getCtgInfo(String ctgName) {

		/**
		 * 1. 전체 테이블 내용 모두 가져옴
		 * 		아마도 실 서비스환경이라면 할수없으나, 예시의 Sample 데이터양이 많지않은 관계로 모든 데이터 조회함
		 * 		만약 데이터가 많은 서비스환경이라면, 컬럼별 index 생성 & index_asc scan 으로 해당컬럼의 가장작은 값을 가지는 row 를 가져올수있음
		 * 2. 대상 카테고리의 최저가격을 구함
		 * 3. 최소값을 가지는 브랜드의 정보를 리턴함
		 */

		ResponseCtgInfoDto result = new ResponseCtgInfoDto();

		String ctg = findCtgByCtgName(ctgName);

		result.setCtgName(ctgName);
		result.setCtg(ctg);

		//param validator
		if(ctg == null) {
			result.setResMsg("카테고리정보없음");
			return result;
		}

		List<Product> products = productRepository.findAll();


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
			int price = getCtgValue(ctg, p);
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
			result.setMinCtgDto(setCtgDto(ctg, minProduct));
		}
		if (maxProduct != null) {
			result.setMaxCtgDto(setCtgDto(ctg, maxProduct));
		}

		if (minProduct == null || maxProduct == null)
		{
			result.setResMsg("정상조회 실패");
		}
		else {
			result.setResMsg("카테고리기준 브랜드정보");
		}

		return result;
	}

	// 4. Name을 키로 Row 추가/변경/삭제
	public String changeInfo(String type, Product product) {

		//type validator
		if(
			!("insert".equals(type)
			|| "update".equals(type)
			|| "delete".equals(type))
		)
		{
			return "insert, update, delete 만 입력가능합니다";
		}
		//key validator
		if(!StringUtils.hasText(product.getBrand()))
		{
			return "Brand 값은 필수입력값입니다";
		}

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

				//db에 존재하는 값 조회
				Product existingProduct = productToUpdate.get();

				//변경입력값 추가
				makeProductEntity(existingProduct, product);

				//update 처리
				productRepository.save(existingProduct);
				return "변경되었습니다.";
			case "delete":
				if (!productRepository.existsById(product.getBrand())) {
					return "존재하지 않는 Brand입니다.";
				}
				productRepository.deleteById(product.getBrand());
				return "삭제되었습니다.";
			default:
				return "유효하지 않은 타입입니다. (insert, update, delete 중 선택)";
		}
	}

	// ctg 정보 -> Db cloumn 응답
	private int getCtgValue(String ctg, Product product) {
		switch (ctg) {
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

	//세부정보 응답객체 set (공통)
	private ResponseCtgDto setCtgDto (String ctg, Product product )
	{
		ResponseCtgDto ctgDto = new ResponseCtgDto();
		BeanUtils.copyProperties(ctgMappingMap.get(ctg), ctgDto);
		ctgDto.setBrand(product.getBrand());
		ctgDto.setPrice(getCtgValue(ctg, product));
		return ctgDto;
	}

	//카테고리명으로 카테고리 컬럼 검색
	private String findCtgByCtgName(String ctgName)
	{
		String ctg = null;
		Optional<Map.Entry<String, ResponseCtgDto>> filter = ctgMappingMap.entrySet().stream()
				.filter(entry -> ctgName.equals(entry.getValue().getCtgName()))
				.findFirst();
		//카테고리
		if(filter.isPresent())
			ctg = filter.get().getKey();

		return ctg;
	}

	//조회된 결과에 입력된 변경값 추가
	private void makeProductEntity(Product existingProduct, Product product)
	{
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
	}
}
