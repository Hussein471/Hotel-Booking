package com.dailyplanner.hotelbookingbackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long roomId,
        String roomNumber,
        String roomType,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        LocalDateTime createdAt
){

}
