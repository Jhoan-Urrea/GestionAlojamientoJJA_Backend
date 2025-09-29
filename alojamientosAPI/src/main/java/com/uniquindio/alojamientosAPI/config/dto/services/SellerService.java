package com.uniquindio.alojamientosAPI.config.dto.services;

import com.uniquindio.alojamientosAPI.config.dto.SellerDTO;
import java.util.List;

public interface SellerService {
    SellerDTO createSeller(SellerDTO sellerDTO);
    SellerDTO getSellerById(Long id);
    List<SellerDTO> getAllSellers();
    SellerDTO updateSeller(Long id, SellerDTO sellerDTO);
    void deleteSeller(Long id);
}
