package com.uniquindio.alojamientosAPI.domain.dto.review;

import java.time.LocalDate;

public class RatingResponse {
    private Long id;
    private Long reservationId;
    private Long userId;
    private Integer rating;
    private Boolean commentable;
    private LocalDate commentExpiration;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Boolean getCommentable() { return commentable; }
    public void setCommentable(Boolean commentable) { this.commentable = commentable; }

    public LocalDate getCommentExpiration() { return commentExpiration; }
    public void setCommentExpiration(LocalDate commentExpiration) { this.commentExpiration = commentExpiration; }
}

