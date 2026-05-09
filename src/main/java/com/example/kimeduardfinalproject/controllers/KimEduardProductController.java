package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardProductResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class KimEduardProductController {

    private final KimEduardProductService productService;

    @GetMapping
    public List<KimEduardProductResponseDTO> getAllVisible() {
        return productService.getAllVisible();
    }

    @GetMapping("/{id}")
    public KimEduardProductResponseDTO getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/search")
    public List<KimEduardProductResponseDTO> search(@RequestParam String name) {
        return productService.searchByName(name);
    }
}