package com.dailyplanner.hotelbookingbackend.entity;

import jakarta.persistence.*;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "room_type", nullable = false, length = 30)
    private String roomType;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "room_Number", nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private boolean available = true;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings = new ArrayList<>();

    public List<Booking> getBookings() {
        return bookings;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }


    public boolean isInService() {
        return available;
    }

    public boolean isBookedToday() {
        LocalDate today = LocalDate.now();
        return bookings.stream().anyMatch(b ->
                !b.getCheckInDate().isAfter(today) && b.getCheckOutDate().isAfter(today)
        );
    }

    public boolean isAvailable() {
        return isInService() && !isBookedToday();
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {

        this.pricePerNight = pricePerNight;
    }

}