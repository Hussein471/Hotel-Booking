package com.dailyplanner.hotelbookingbackend.repository;

import com.dailyplanner.hotelbookingbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomNumber(String roomNumber);
}
