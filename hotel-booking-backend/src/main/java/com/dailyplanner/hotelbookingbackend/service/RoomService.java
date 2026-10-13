package com.dailyplanner.hotelbookingbackend.service;

import com.dailyplanner.hotelbookingbackend.dto.RoomRequest;
import com.dailyplanner.hotelbookingbackend.dto.RoomResponse;
import com.dailyplanner.hotelbookingbackend.entity.Room;
import com.dailyplanner.hotelbookingbackend.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public  class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    public RoomResponse createRoom(RoomRequest request) {

        if(roomRepository.existsByRoomNumber(request.roomNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room number already exists"
            );
        }

        Room room = new Room();
        room.setRoomNumber(request.roomNumber());
        room.setRoomType(request.roomType());
        room.setPricePerNight(request.pricePerNight());
        room.setAvailable(request.available());

        Room savedRoom = roomRepository.save(room);
        return toResponse(savedRoom);

    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RoomResponse getRoomById(Long id){
        return toResponse(getRoomEntityById(id));
    }

    public RoomResponse updateRoom(Long id, RoomRequest request){
        Room room = getRoomEntityById(id);

        //kuzuia duplication
        boolean roomNumberChanged =
                !room.getRoomNumber().equals(request.roomNumber());

        if(roomNumberChanged &&
        roomRepository.existsByRoomNumber(request.roomNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Room number already exists"
            );
        }

        room.setRoomNumber(request.roomNumber());
        room.setRoomType(request.roomType());
        room.setPricePerNight(request.pricePerNight());
        room.setAvailable(request.available());

        Room updatedRoom = roomRepository.save(room);
        return toResponse(updatedRoom);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomEntityById(id);

        roomRepository.delete(room);
    }

    private Room getRoomEntityById(Long id){
        return roomRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room not found"
                ));
    }

    //kubadlisha room entity kuwa ROOMResponse
    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight(),
                room.isAvailable()
        );
    }
}
