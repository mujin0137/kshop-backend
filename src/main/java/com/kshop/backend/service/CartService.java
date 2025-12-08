package com.kshop.backend.service;

import com.kshop.backend.dto.*;
import com.kshop.backend.entity.*;
import com.kshop.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public CartDTO getCart(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseGet(() -> createCart(user));
        
        return convertToDTO(cart);
    }
    
    public CartDTO addToCart(String email, AddToCartRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseGet(() -> createCart(user));
        
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // 이미 장바구니에 있는지 확인
        CartItem existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(request.getProductId()))
            .findFirst()
            .orElse(null);
        
        if (existingItem != null) {
            // 수량만 증가
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            // 새 아이템 추가
            CartItem newItem = CartItem.builder()
                .product(product)
                .quantity(request.getQuantity())
                .build();
            cart.addItem(newItem);
        }
        
        cartRepository.save(cart);
        return convertToDTO(cart);
    }
    
    public CartDTO updateCartItem(String email, Long itemId, UpdateCartItemRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem item = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        
        return convertToDTO(cart);
    }
    
    public CartDTO removeCartItem(String email, Long itemId) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem item = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        cart.removeItem(item);
        cartItemRepository.delete(item);
        
        return convertToDTO(cart);
    }
    
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    private Cart createCart(User user) {
        Cart cart = Cart.builder()
            .user(user)
            .build();
        return cartRepository.save(cart);
    }
    
    private CartDTO convertToDTO(Cart cart) {
        int totalPrice = cart.getItems().stream()
            .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();
        
        return CartDTO.builder()
            .id(cart.getId())
            .items(cart.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList()))
            .totalPrice(totalPrice)
            .build();
    }
    
    private CartItemDTO convertItemToDTO(CartItem item) {
        return CartItemDTO.builder()
            .id(item.getId())
            .productId(item.getProduct().getId())
            .productTitle(item.getProduct().getTitle())
            .productPrice(item.getProduct().getPrice())
            .productImage(item.getProduct().getImage())
            .quantity(item.getQuantity())
            .build();
    }
}
