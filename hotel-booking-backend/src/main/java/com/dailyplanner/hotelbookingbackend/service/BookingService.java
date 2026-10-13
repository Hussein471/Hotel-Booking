package com.dailyplanner.hotelbookingbackend.service;

import com.dailyplanner.hotelbookingbackend.dto.AdminBookingResponse;
import com.dailyplanner.hotelbookingbackend.dto.BookingRequest;
import com.dailyplanner.hotelbookingbackend.dto.BookingResponse;
import com.dailyplanner.hotelbookingbackend.entity.AppUser;
import com.dailyplanner.hotelbookingbackend.entity.Booking;
import com.dailyplanner.hotelbookingbackend.entity.Room;
import com.dailyplanner.hotelbookingbackend.repository.AppUserRepository;
import com.dailyplanner.hotelbookingbackend.repository.BookingRepository;
import com.dailyplanner.hotelbookingbackend.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService{

    private final BookingRepository bookingRepository;
    private final AppUserRepository appUserRepository;
    private final RoomRepository roomRepository;

    public BookingService(
            BookingRepository bookingRepository,
            AppUserRepository appUserRepository,
            RoomRepository roomRepository
    ){
        this.bookingRepository = bookingRepository;
        this.appUserRepository = appUserRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public BookingResponse createBooking(
            String userEmail,
            BookingRequest request
    ) {
        //user kw email iliyotumika
        AppUser user = getUserByEmail(userEmail);

        //tunatafuta chumba
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Room not found"
                ));

        if(!room.isInService()){
            throw  new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room is under maintenance");
        }

        if(!request.checkOutDate().isAfter(request.checkInDate())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "check-out date must be after check-in date"
            );
        }

        boolean roomAlreadyBooked =
                bookingRepository
                        .existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                room,
                                request.checkOutDate(),
                                request.checkInDate()
                        );

        if(roomAlreadyBooked){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "room already booked for these dates"
            );
        }

        //boooking object
        Booking booking = new Booking();
        //kuonganisha booking na user
        booking.setUser(user);
        booking.setRoom(room);

        booking.setCheckInDate(request.checkInDate());
        booking.setCheckOutDate(request.checkOutDate());

        booking.setCreatedAt(LocalDateTime.now());


        Booking savedBooking = bookingRepository.save(booking);
        return toResponse(savedBooking);
    }


    public List<BookingResponse> getMyBookings(String userEmail) {
        AppUser user = getUserByEmail(userEmail);

        return bookingRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AppUser getUserByEmail(String email){
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }


    private BookingResponse toResponse(Booking booking){
        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getRoom().getRoomNumber(),
                booking.getRoom().getRoomType(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getCreatedAt()
        );
    }

    public List<AdminBookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(b -> new AdminBookingResponse(
                        b.getId(),
                        b.getUser().getName(),
                        b.getUser().getEmail(),
                        b.getRoom().getRoomType(),
                        b.getRoom().getRoomNumber(),
                        b.getCheckInDate(),
                        b.getCheckOutDate(),
                        b.getCreatedAt()
                ))
                .toList();
    }
}














