package com.uniquindio.alojamientosAPI.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCommentDTO {
    private Long id;
    @NotNull
    private Long reservationId;
    @NotNull
    private Long guestId;
    @NotBlank
    private String comment;
}
