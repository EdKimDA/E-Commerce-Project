package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardLoginRequestDTO;
import com.example.kimeduardfinalproject.dto.requests.KimEduardUserRegisterRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardLoginResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardUserResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KimEduardAuthController {

    private final KimEduardUserService userService;

    @PostMapping("/register")
    public ResponseEntity<KimEduardUserResponseDTO> register(@Valid @RequestBody KimEduardUserRegisterRequestDTO dto) {
        KimEduardUserResponseDTO response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public KimEduardLoginResponseDTO login(@Valid @RequestBody KimEduardLoginRequestDTO dto) {
        return userService.login(dto);
    }
}