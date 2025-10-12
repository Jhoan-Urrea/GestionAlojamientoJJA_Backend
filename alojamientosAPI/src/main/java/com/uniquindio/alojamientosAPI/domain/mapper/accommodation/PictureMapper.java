package com.uniquindio.alojamientosAPI.domain.mapper.accommodation;

import java.util.List;

import org.mapstruct.Mapper;

import com.uniquindio.alojamientosAPI.domain.dto.accommodation.PictureDTO;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.PictureEntity;

@Mapper(componentModel = "spring")
public interface PictureMapper {
    PictureDTO toDto(PictureEntity entity);
    List<PictureDTO> toDtoList(List<PictureEntity> entityList);
}
