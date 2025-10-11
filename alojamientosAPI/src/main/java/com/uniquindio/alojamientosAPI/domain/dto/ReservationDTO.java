package com.uniquindio.alojamientosAPI.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservationDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long accommodationId;
    @NotNull
    private String state;
    @NotNull
    private java.time.LocalDate startDate;
    @NotNull
    private java.time.LocalDate endDate;
    @NotNull
    @Size(min = 1)
    private java.util.List<Long> guestIds;
}
