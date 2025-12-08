package com.kshop.backend.controller;

import com.kshop.backend.dto.*;
import com.kshop.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    @GetMapping
    public ResponseEntity<CartDTO> getCart(Authentication authentication) {
        String email = authentication.getName();
        CartDTO cart = cartService.getCart(email);
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addToCart(
            Authentication authentication,
            @RequestBody AddToCartRequest request) {
        String email = authentication.getName();
        CartDTO cart = cartService.addToCart(email, request);
        return ResponseEntity.ok(cart);
    }
    
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDTO> updateCartItem(
            Authentication authentication,
            @PathVariable Long itemId,
            @RequestBody UpdateCartItemRequest request) {
        String email = authentication.getName();
        CartDTO cart = cartService.updateCartItem(email, itemId, request);
        return ResponseEntity.ok(cart);
    }
    
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDTO> removeCartItem(
            Authentication authentication,
            @PathVariable Long itemId) {
        String email = authentication.getName();
        CartDTO cart = cartService.removeCartItem(email, itemId);
        return ResponseEntity.ok(cart);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        String email = authentication.getName();
        cartService.clearCart(email);
        return ResponseEntity.ok().build();
    }
}
