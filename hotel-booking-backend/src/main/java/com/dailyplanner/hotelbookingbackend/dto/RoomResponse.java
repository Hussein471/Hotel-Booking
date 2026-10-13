package com.dailyplanner.hotelbookingbackend.dto;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String roomNumber,
        String roomType,
        BigDecimal pricePerNight,
        boolean available
)
{

}
