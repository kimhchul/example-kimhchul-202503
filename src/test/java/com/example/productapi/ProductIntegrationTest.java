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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		// 테스트 전 데이터 초기화
		productRepository.deleteAll();

		// 테스트 데이터 삽입
		productRepository.save(new Product("A", 11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300));
		productRepository.save(new Product("B", 10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200));
		// 나머지 데이터 추가...
	}

	@Test
	void testGetLowPriceByCtg() {
		ResponseEntity<Map> response = restTemplate.getForEntity("/query/getLowPriceByCtg", Map.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Map<String, Object> body = response.getBody();
		assertThat(body).isNotNull();

		assertThat(body.get("ctg1")).isEqualTo(Map.of("name", "B", "price", 10500));

///		restTemplate.
	}



}
