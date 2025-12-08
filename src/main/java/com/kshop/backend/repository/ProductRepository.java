package com.kshop.backend.repository;

import com.kshop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // 카테고리별 상품 조회
    List<Product> findByCategory(String category);
    
    // 신상품 조회
    List<Product> findByIsNewTrue();
}
