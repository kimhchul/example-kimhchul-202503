package com.example.productapi;

import com.example.productapi.entity.Product;
import com.example.productapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// 랜덤 포트를 사용하여 실제 웹 환경에서 테스트
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ProductRepository productRepository;

	//테스트 데이터 초기화
	@BeforeEach
	void setUp() {
		// 테스트 전 데이터 초기화
		productRepository.deleteAll();

		// 테스트 데이터 삽입
		productRepository.save(new Product("A", 11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300));
		productRepository.save(new Product("B", 10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200));
		productRepository.save(new Product("H", 10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000));
		productRepository.save(new Product("I", 11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400));

	}

	/**
	 * getLowPriceByCtg 테스트
	 * setup된 데이터 기준으로 일부 응답결과 (totalSum, ctgList 사이즈등)에 대한 확인
	 */
	@Test
	void testGetLowPriceByCtg() {
		ResponseEntity<Map> response = restTemplate.getForEntity("/query/getLowPriceByCtg", Map.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Map<String, Object> body = response.getBody();
		assertThat(body).isNotNull();




		assertThat(body.get("totalSum")).isEqualTo(35400);

		List ctgList = (List) body.get("ctgList");
		assertThat(ctgList.size()).isEqualTo(8);
	}

	/**
	 * getLowPriceByBrand 테스트
	 * setup된 데이터 기준으로 일부 응답결과 (totalSum, brand 이름)에 대한 확인
	 */
	@Test
	void testGetLowPriceByBrand() {
		ResponseEntity<Map> response = restTemplate.getForEntity("/query/getLowPriceByBrand", Map.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Map<String, Object> body = response.getBody();
		assertThat(body).isNotNull();

		assertThat(body.get("totalSum")).isEqualTo(37600);
		assertThat(body.get("brand")).isEqualTo("B");
	}


	/**
	 * getCtgInfo?ctgName=상의 테스트
	 * setup된 데이터 기준의 상의 카테고리에 대하여 응답결과 (최소카테고리, 최고카테고리)에 대한 확인
	 */
	@Test
	void testGetCtgInfo() {
		ResponseEntity<Map> response = restTemplate.getForEntity("/query/getCtgInfo?ctgName=상의", Map.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Map<String, Object> body = response.getBody();
		assertThat(body).isNotNull();

		assertThat(body.get("minCtgDto")).isEqualTo(Map.of("brand","B", "ctg","ctgTops", "ctgName","상의", "price",10500, "sortOrder",1));
		assertThat(body.get("maxCtgDto")).isEqualTo(Map.of("brand","I", "ctg","ctgTops", "ctgName","상의", "price",11400, "sortOrder",1));

	}

	/**
	 * changeInfo 테스트
	 * 	새로운 product를 insert -> update -> delete 에 대한 응답 확인
	 */
	@Test
	void testChangeCtgCtgInfo() {

		//Insert
		Product p1 = new Product("K", 11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300);
		ResponseEntity<String> testInsertResponse = restTemplate.postForEntity("/command/changeInfo?type=insert", p1, String.class);
		assertThat(testInsertResponse.getStatusCodeValue()).isEqualTo(200);
		String testInsertResponseStr = testInsertResponse.getBody();
		assertThat(testInsertResponseStr).isNotNull();
		assertThat(testInsertResponseStr).isEqualTo("추가되었습니다.");

		//Update
		Product p2 = new Product("K", 100, 200, 300, 400, 500, 600, 700, 800);
		ResponseEntity<String> testUpdateResponse = restTemplate.postForEntity("/command/changeInfo?type=update", p2, String.class);
		assertThat(testUpdateResponse.getStatusCodeValue()).isEqualTo(200);
		String testUpdateResponseStr = testUpdateResponse.getBody();
		assertThat(testUpdateResponseStr).isNotNull();
		assertThat(testUpdateResponseStr).isEqualTo("변경되었습니다.");

		//Delete
		Product p3 = new Product();
		p3.setBrand("K");
		ResponseEntity<String> testDeleteresponse = restTemplate.postForEntity("/command/changeInfo?type=delete", p3, String.class);
		assertThat(testDeleteresponse.getStatusCodeValue()).isEqualTo(200);
		String testDeleteResponseStr = testDeleteresponse.getBody();
		assertThat(testDeleteResponseStr).isNotNull();
		assertThat(testDeleteResponseStr).isEqualTo("삭제되었습니다.");
	}



}
