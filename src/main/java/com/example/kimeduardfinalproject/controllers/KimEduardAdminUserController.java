package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardAdminCreateUserRequestDTO;
import com.example.kimeduardfinalproject.dto.requests.KimEduardUserUpdateRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardUserResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class KimEduardAdminUserController {

    private final KimEduardUserService userService;

    @PostMapping
    public ResponseEntity<KimEduardUserResponseDTO> createUser(@Valid @RequestBody KimEduardAdminCreateUserRequestDTO dto) {
        KimEduardUserResponseDTO response = userService.adminCreateUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<KimEduardUserResponseDTO> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public KimEduardUserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public KimEduardUserResponseDTO updateUser(
            @PathVariable Long id,
            @Valid @RequestBody KimEduardUserUpdateRequestDTO dto
    ) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.softDelete(id);
    }
}