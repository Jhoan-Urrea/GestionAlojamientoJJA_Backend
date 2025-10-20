package com.uniquindio.alojamientosAPI.domain.dto.reservation;

import java.time.LocalDate;

public class ReservationResponse {
    private Long id;
    private Long userId;
    private Long accommodationId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer countRoommates;
    private String stateName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAccommodationId() { return accommodationId; }
    public void setAccommodationId(Long accommodationId) { this.accommodationId = accommodationId; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public Integer getCountRoommates() { return countRoommates; }
    public void setCountRoommates(Integer countRoommates) { this.countRoommates = countRoommates; }

    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
}

