package com.uniquindio.alojamientosAPI.domain.mapper.accommodation;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.uniquindio.alojamientosAPI.domain.dto.accommodation.AccommodationDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;


@Mapper(componentModel = "spring", uses = {ServiceMapper.class, PictureMapper.class})
public interface AccommodationMapper {

    @Mappings({
        // ✅ Campos simples
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "title", target = "title"),
        @Mapping(source = "description", target = "description"),
        @Mapping(source = "address", target = "address"),
        @Mapping(source = "dayPrice", target = "dayPrice"),
        @Mapping(source = "capacity", target = "capacity"),

        // ✅ Campos derivados desde relaciones
        @Mapping(source = "stateAccommodation.name", target = "stateName"),
        @Mapping(source = "city.name", target = "cityName"),
        @Mapping(source = "hostUser.firstName", target = "hostName"),

        // ✅ Relaciones (ya existen en tu DTO)
        @Mapping(source = "pictures", target = "pictures"),
        @Mapping(source = "services", target = "services")
    })
    AccommodationDTO toDto(AccommodationEntity entity);

    @Mappings({
        // ✅ Mapeo inverso: desde DTO a entidad
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "title", target = "title"),
        @Mapping(source = "description", target = "description"),
        @Mapping(source = "address", target = "address"),
        @Mapping(source = "dayPrice", target = "dayPrice"),
        @Mapping(source = "capacity", target = "capacity"),

        // 🚫 Ignoramos relaciones (existen en la entidad, pero no en el DTO)
        @Mapping(target = "hostUser", ignore = true),
        @Mapping(target = "stateAccommodation", ignore = true),
        @Mapping(target = "city", ignore = true),
        @Mapping(target = "pictures", ignore = true),
        @Mapping(target = "services", ignore = true),
        @Mapping(target = "favorites", ignore = true),
        @Mapping(target = "reservations", ignore = true)
    })
    AccommodationEntity toEntity(AccommodationDTO dto);

    List<AccommodationDTO> toDtoList(List<AccommodationEntity> entities);
}
