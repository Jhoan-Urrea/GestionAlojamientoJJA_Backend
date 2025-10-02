package com.uniquindio.alojamientosAPI.config.dto.services;

import com.uniquindio.alojamientosAPI.config.dto.ProductDTO;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO getProductById(Long id);
}

