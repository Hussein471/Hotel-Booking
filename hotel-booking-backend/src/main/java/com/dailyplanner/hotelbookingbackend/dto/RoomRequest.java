package com.dailyplanner.hotelbookingbackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RoomRequest(
        @NotBlank(message = "Room Number is required")
        String roomNumber,

        @NotBlank(message = "Room type is required")
        String roomType,

        @NotNull(message = "price per night is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
                BigDecimal pricePerNight,

        @NotNull(message = "Availability is required")
        Boolean available



){

}

