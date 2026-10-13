package com.dailyplanner.hotelbookingbackend.controller;

// HII ndiyo Authentication sahihi ya Spring Security.
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @GetMapping("/me")
    public Map<String, String> getCurrentUser(Authentication authentication) {

        // getName() hutoa email tuliyoweka kwenye JWT filter.
        return Map.of("email", authentication.getName());
    }
}