package com.dailyplanner.hotelbookingbackend.controller;


import com.dailyplanner.hotelbookingbackend.dto.LoginRequest;
import com.dailyplanner.hotelbookingbackend.dto.LoginResponse;
import com.dailyplanner.hotelbookingbackend.dto.RegisterRequest;
import com.dailyplanner.hotelbookingbackend.dto.RegisterResponse;
import com.dailyplanner.hotelbookingbackend.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {

        this.appUserService = appUserService;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = appUserService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody LoginRequest request
    ) {
        LoginResponse response = appUserService.login(request);
        return ResponseEntity.ok(response);
    }



}
