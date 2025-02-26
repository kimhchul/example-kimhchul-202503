package com.example.productapi.repository;

import com.example.productapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
	List<Product> findTopByCtg1(int ctg1);
	// 추가적인 쿼리 메서드가 필요하면 선언
}
