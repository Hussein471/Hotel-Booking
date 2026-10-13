package com.dailyplanner.hotelbookingbackend.controller;


import com.dailyplanner.hotelbookingbackend.dto.BookingRequest;
import com.dailyplanner.hotelbookingbackend.dto.BookingResponse;
import com.dailyplanner.hotelbookingbackend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController{

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid
            @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        BookingResponse response =
                bookingService.createBooking(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                bookingService.getMyBookings(userEmail)
        );
    }
}















