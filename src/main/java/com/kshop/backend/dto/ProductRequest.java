package com.kshop.backend.dto;
import lombok.Data;
@Data
public class ProductRequest {
    private String title;
    private Integer price;
    private Integer originalPrice;
    private String image;
    private Double rating;
    private Integer reviewCount;
    private Boolean isNew;
    private String category;
}
