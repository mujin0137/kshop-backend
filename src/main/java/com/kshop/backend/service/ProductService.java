package com.kshop.backend.service;
import com.kshop.backend.dto.ProductRequest;
import com.kshop.backend.entity.Product;
import com.kshop.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> getNewProducts() {
        return productRepository.findByIsNewTrue();
    }
    
    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = Product.builder()
            .title(request.getTitle())
            .price(request.getPrice())
            .originalPrice(request.getOriginalPrice())
            .image(request.getImage())
            .rating(request.getRating() != null ? request.getRating() : 0.0)
            .reviewCount(request.getReviewCount() != null ? request.getReviewCount() : 0)
            .isNew(request.getIsNew() != null ? request.getIsNew() : false)
            .category(request.getCategory())
            .build();
        
        return productRepository.save(product);
    }
    
    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);
        
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setImage(request.getImage());
        product.setRating(request.getRating() != null ? request.getRating() : product.getRating());
        product.setReviewCount(request.getReviewCount() != null ? request.getReviewCount() : product.getReviewCount());
        product.setIsNew(request.getIsNew() != null ? request.getIsNew() : product.getIsNew());
        product.setCategory(request.getCategory());
        
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
