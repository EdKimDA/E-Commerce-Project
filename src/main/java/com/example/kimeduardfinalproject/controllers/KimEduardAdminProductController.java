package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardProductRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardProductResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class KimEduardAdminProductController {

    private final KimEduardProductService productService;

    @PostMapping
    public ResponseEntity<KimEduardProductResponseDTO> createProduct(@Valid @RequestBody KimEduardProductRequestDTO dto) {
        KimEduardProductResponseDTO response = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<KimEduardProductResponseDTO> getAllForAdmin() {
        return productService.getAllForAdmin();
    }

    @GetMapping("/{id}")
    public KimEduardProductResponseDTO getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public KimEduardProductResponseDTO updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody KimEduardProductRequestDTO dto
    ) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.softDelete(id);
    }

    @PostMapping("/{id}/image")
    public KimEduardProductResponseDTO uploadProductImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return productService.uploadProductImage(id, file);
    }

    @DeleteMapping("/{id}/image")
    public void deleteProductImage(@PathVariable Long id) {
        productService.deleteProductImage(id);
    }
}