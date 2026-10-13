package com.dailyplanner.hotelbookingbackend.repository;

import com.dailyplanner.hotelbookingbackend.entity.AppUser;
import com.dailyplanner.hotelbookingbackend.entity.Booking;
import com.dailyplanner.hotelbookingbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Room room,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );

    List<Booking> findByUserOrderByCreatedAtDesc(AppUser user);
}