package com.dailyplanner.hotelbookingbackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminBookingResponse (
    Long id,
    String userName,
    String userEmail,
    String roomType,
    String roomNumber,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    LocalDateTime createdAt
){

}
