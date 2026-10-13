package com.dailyplanner.hotelbookingbackend.controller;


import com.dailyplanner.hotelbookingbackend.dto.RoomRequest;
import com.dailyplanner.hotelbookingbackend.dto.RoomResponse;
import com.dailyplanner.hotelbookingbackend.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")

public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    //Post
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid
            @RequestBody RoomRequest request
    ) {
        RoomResponse response = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }


    //get
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Long id
    )
    {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }



    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid
            @RequestBody RoomRequest request
    ) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoomResponse> deleteRoom(
            @PathVariable Long id
    ){
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}

