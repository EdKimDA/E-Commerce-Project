package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardLoginRequestDTO;
import com.example.kimeduardfinalproject.dto.requests.KimEduardUserRegisterRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardLoginResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardUserResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication",
        description = "Endpoints for user registration and login"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KimEduardAuthController {

    private final KimEduardUserService userService;

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account. The user role is always USER by default."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    public ResponseEntity<KimEduardUserResponseDTO> register(@Valid @RequestBody KimEduardUserRegisterRequestDTO dto) {
        KimEduardUserResponseDTO response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticates user by username and password and returns JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "Invalid username or password")
    })
    public KimEduardLoginResponseDTO login(@Valid @RequestBody KimEduardLoginRequestDTO dto) {
        return userService.login(dto);
    }
}