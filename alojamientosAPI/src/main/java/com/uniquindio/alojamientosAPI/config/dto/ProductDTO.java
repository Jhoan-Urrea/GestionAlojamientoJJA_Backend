package com.uniquindio.alojamientosAPI.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {


    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private Long sellerId;

    private String sellerName;

    private String sellerEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}