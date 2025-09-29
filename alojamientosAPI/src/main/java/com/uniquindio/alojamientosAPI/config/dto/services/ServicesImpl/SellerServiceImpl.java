package com.uniquindio.alojamientosAPI.config.dto.services.ServicesImpl;



import com.uniquindio.alojamientosAPI.config.dto.SellerDTO;
import com.uniquindio.alojamientosAPI.config.dto.services.SellerService;


import java.util.HashMap;
import java.util.Map;

public class SellerServiceImpl implements SellerService {

    private final Map<Long, SellerDTO> sellerDB = new HashMap<>();

    public SellerServiceImpl() {
        // Datos simulados de ejemplo
        sellerDB.put(1L, new SellerDTO(1L, "María González Tech", "maria.gonzalez@techstore.com"));
        sellerDB.put(2L, new SellerDTO(2L, "Carlos Pérez", "carlos.perez@ventas.com"));
    }

    @Override
    public SellerDTO getSellerById(Long id) {
        return sellerDB.get(id);
    }
}
