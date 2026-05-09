package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardUserUpdateRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardUserResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class KimEduardUserController {

    private final KimEduardUserService userService;

    @GetMapping("/me")
    public KimEduardUserResponseDTO getMe() {
        return userService.getCurrentUser();
    }

    @PutMapping("/me")
    public KimEduardUserResponseDTO updateMe(@Valid @RequestBody KimEduardUserUpdateRequestDTO dto) {
        Long currentUserId = userService.getCurrentUserEntity().getId();
        return userService.update(currentUserId, dto);
    }
}