package com.uniquindio.alojamientosAPI.domain.mapper.accommodation;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.uniquindio.alojamientosAPI.domain.dto.accommodation.ServiceDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.ServiceEntity;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    // Entity a DTO
    @Mapping(source = "category", target = "category")
    ServiceDTO toDto(ServiceEntity service);

    List<ServiceDTO> toDtoList(List<ServiceEntity> services);

    // Sólo si necesitas la conversión inversa
    @Mapping(target = "category", ignore = true)
    ServiceEntity toEntity(ServiceDTO dto);
}
