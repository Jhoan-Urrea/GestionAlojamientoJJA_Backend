package com.uniquindio.alojamientosAPI.web;

import com.uniquindio.alojamientosAPI.domain.dto.GuestRatingDTO;
import com.uniquindio.alojamientosAPI.domain.dto.ReviewCommentDTO;
import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.domain.mapper.GuestRatingMapper;
import com.uniquindio.alojamientosAPI.domain.mapper.ReviewCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{id}/rate")
    public GuestRatingDTO rateReservation(@PathVariable Long id, @RequestBody RateRequest request) {
        var entity = reviewService.rateReservation(id, request.getGuestId(), request.getRating());
        return GuestRatingMapper.toDTO(entity);
    }

    @PostMapping("/{id}/comment")
    public ReviewCommentDTO commentReservation(@PathVariable Long id, @RequestBody CommentRequest request) {
        var entity = reviewService.commentReservation(id, request.getGuestId(), request.getComment());
        return ReviewCommentMapper.toDTO(entity);
    }

    // DTOs para las peticiones
    public static class RateRequest {
        private Long guestId;
        private int rating;
        public Long getGuestId() { return guestId; }
        public void setGuestId(Long guestId) { this.guestId = guestId; }
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }
    public static class CommentRequest {
        private Long guestId;
        private String comment;
        public Long getGuestId() { return guestId; }
        public void setGuestId(Long guestId) { this.guestId = guestId; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
