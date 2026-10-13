package com.dailyplanner.hotelbookingbackend.service;


import com.dailyplanner.hotelbookingbackend.entity.AppUser;
import com.dailyplanner.hotelbookingbackend.repository.AppUserRepository;
import com.dailyplanner.hotelbookingbackend.dto.LoginRequest;
import com.dailyplanner.hotelbookingbackend.dto.LoginResponse;
import com.dailyplanner.hotelbookingbackend.dto.RegisterRequest;
import com.dailyplanner.hotelbookingbackend.dto.RegisterResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AppUserService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (appUserRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        AppUser user = new AppUser();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        AppUser savedUser = appUserRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );

    }

    public LoginResponse login(LoginRequest request) {

        AppUser user = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password."
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password."
            );
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token);
    }
}
