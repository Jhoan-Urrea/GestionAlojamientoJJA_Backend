package com.uniquindio.alojamientosAPI.web;

import com.uniquindio.alojamientosAPI.domain.dto.GuestRatingDTO;
import com.uniquindio.alojamientosAPI.domain.dto.ReviewCommentDTO;
import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.domain.mapper.GuestRatingMapper;
import com.uniquindio.alojamientosAPI.domain.mapper.ReviewCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/reservations")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateReservation(@PathVariable Long id, @Valid @RequestBody RateRequest request) {
        var entity = reviewService.rateReservation(id, request.getGuestId(), request.getRating());
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede calificar: reserva no finalizada o ya calificada.");
        }
        return ResponseEntity.ok(GuestRatingMapper.toDTO(entity));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> commentReservation(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        var entity = reviewService.commentReservation(id, request.getGuestId(), request.getComment());
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede comentar: reserva no finalizada o ya comentada.");
        }
        return ResponseEntity.ok(ReviewCommentMapper.toDTO(entity));
    }

    // DTOs para las peticiones
    public static class RateRequest {
        @NotNull
        private Long guestId;
        @Min(1)
        @Max(5)
        private int rating;
        public Long getGuestId() { return guestId; }
        public void setGuestId(Long guestId) { this.guestId = guestId; }
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }
    public static class CommentRequest {
        @NotNull
        private Long guestId;
        @NotNull
        private String comment;
        public Long getGuestId() { return guestId; }
        public void setGuestId(Long guestId) { this.guestId = guestId; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
