package com.dailyplanner.hotelbookingbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingRequest(

        @NotNull(message = "Room id is required")
        @Positive(message = "Room id must be positive")
        Long roomId,

        @NotNull(message = "Check-in date is required")
        @FutureOrPresent(message = "check-in date cannot be inthe past")
        LocalDate checkInDate,

        @NotNull(message = "Check-out date is required")
        @Future(message = "Check-out date must be in future")
        LocalDate checkOutDate


){

}
