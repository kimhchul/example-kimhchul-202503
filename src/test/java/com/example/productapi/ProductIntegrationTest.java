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

import java.util.List;
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
		productRepository.save(new Product("H", 10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000));
		productRepository.save(new Product("I", 11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400));


	}

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

	@Test
	void testGetLowPriceByName() {
		ResponseEntity<Map> response = restTemplate.getForEntity("/query/getLowPriceByName", Map.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Map<String, Object> body = response.getBody();
		assertThat(body).isNotNull();

		assertThat(body.get("totalSum")).isEqualTo(37600);
		assertThat(body.get("ctg")).isEqualTo("B");
	}

	@Test
	void testGetCtgInfo() {
		ResponseEntity<Map> response = restTemplate.getForEntity("/query/getCtgInfo?ctgName=ctg6", Map.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Map<String, Object> body = response.getBody();
		assertThat(body).isNotNull();

		assertThat(body.get("minProduct")).isEqualTo(Map.of("brand", "H", "ctg1",10800, "ctg2",6300, "ctg3",3100, "ctg4",9700
				, "ctg5",2100, "ctg6",1600, "ctg7",2000, "ctg8",2000));

	}


}
