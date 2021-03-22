package com.ayush.discussionforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name required")
    @Size(min = 1, max = 30, message = "must be between 2 - 30 characters")
    private String firstName;
    @NotBlank(message = "Last name required")
    @Size(min = 1, max = 30, message = "must be between 2 - 30 characters")
    private String lastName;
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "must be between 3 - 15 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 10, max = 30, message = "must be between 10 - 30 characters")
    private String password;
    private String cfUsername;
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;
    @AssertTrue(message = "must agree to the terms")
    private boolean agreedToTerms;
}