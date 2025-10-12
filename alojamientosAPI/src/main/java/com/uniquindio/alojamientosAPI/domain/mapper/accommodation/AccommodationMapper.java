package com.uniquindio.alojamientosAPI.domain.mapper.accommodation;



import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.uniquindio.alojamientosAPI.domain.dto.accommodation.AccommodationDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class, PictureMapper.class})
public interface AccommodationMapper {

    AccommodationMapper INSTANCE = Mappers.getMapper(AccommodationMapper.class);

    // Entity a DTO
    @Mapping(source = "stateAccommodation.name", target = "stateName")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "hostUser.firstName", target = "hostName")
    AccommodationDTO toDto(AccommodationEntity accommodation);

    List<AccommodationDTO> toDtoList(List<AccommodationEntity> accommodations);

    // DTO a Entity (útil para POST/PUT si lo necesitas)
    @Mapping(target = "stateAccommodation", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "hostUser", ignore = true)
    AccommodationEntity toEntity(AccommodationDTO dto);
}
