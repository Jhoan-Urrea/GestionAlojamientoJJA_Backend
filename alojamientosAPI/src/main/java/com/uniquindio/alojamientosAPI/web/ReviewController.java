package com.uniquindio.alojamientosAPI.web;

import com.uniquindio.alojamientosAPI.domain.services.ReviewService;
import com.uniquindio.alojamientosAPI.persistence.entity.GuestRating;
import com.uniquindio.alojamientosAPI.persistence.entity.ReviewComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{id}/rate")
    public GuestRating rateReservation(@PathVariable Long id, @RequestBody RateRequest request) {
        return reviewService.rateReservation(id, request.getGuestId(), request.getRating());
    }

    @PostMapping("/{id}/comment")
    public ReviewComment commentReservation(@PathVariable Long id, @RequestBody CommentRequest request) {
        return reviewService.commentReservation(id, request.getGuestId(), request.getComment());
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
