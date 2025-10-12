package com.uniquindio.alojamientosAPI.domain.dto.accommodation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDTO {
    private Integer id;
    private String title;
    private String description;
    private CategoryDTO category;
}

