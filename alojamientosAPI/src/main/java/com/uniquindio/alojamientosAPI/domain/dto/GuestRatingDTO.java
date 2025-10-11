package com.uniquindio.alojamientosAPI.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestRatingDTO {
    private Long id;
    @NotNull
    private Long reservationId;
    @NotNull
    private Long guestId;
    @Min(1)
    @Max(5)
    private int rating;
}
