package com.kshop.backend.config;

import com.kshop.backend.entity.Product;
import com.kshop.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) {
        try {
            log.info("데이터 초기화 시작...");
            initializeProducts();
            log.info("데이터 초기화 완료!");
        } catch (Exception e) {
            log.error("데이터 초기화 중 오류 발생: {}", e.getMessage());
            log.warn("애플리케이션은 계속 실행됩니다. 데이터베이스 연결을 확인하세요.");
        }
    }
    
    private void initializeProducts() {
        long count = productRepository.count();
        log.info("현재 제품 수: {}", count);
        
        if (count == 0) {
            log.info("샘플 제품 데이터 생성 중...");
            
            Product product1 = Product.builder()
                    .title("삼성 갤럭시 북4 프로 360")
                    .price(1890000)
                    .image("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400")
                    .rating(4.8)
                    .reviewCount(234)
                    .isNew(true)
                    .category("electronics")
                    .build();
            
            Product product2 = Product.builder()
                    .title("LG 그램 17인치 울트라북")
                    .price(2340000)
                    .image("https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=400")
                    .rating(4.7)
                    .reviewCount(189)
                    .isNew(true)
                    .category("electronics")
                    .build();
            
            Product product3 = Product.builder()
                    .title("Apple MacBook Pro 14")
                    .price(2890000)
                    .image("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400")
                    .rating(4.9)
                    .reviewCount(456)
                    .isNew(false)
                    .category("electronics")
                    .build();
            
            Product product4 = Product.builder()
                    .title("소니 WH-1000XM5 헤드폰")
                    .price(450000)
                    .image("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400")
                    .rating(4.9)
                    .reviewCount(678)
                    .isNew(false)
                    .category("electronics")
                    .build();
            
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            
            log.info("샘플 제품 {} 개 생성 완료!", 4);
        } else {
            log.info("기존 제품 데이터가 존재하므로 초기화를 건너뜁니다.");
        }
    }
}
