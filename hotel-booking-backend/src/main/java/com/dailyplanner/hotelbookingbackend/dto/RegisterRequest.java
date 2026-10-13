package com.dailyplanner.hotelbookingbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    String email,

    @NotBlank(message = "Passsword is required")
    @Size(min = 4, max = 100)
    String password
) {

}

