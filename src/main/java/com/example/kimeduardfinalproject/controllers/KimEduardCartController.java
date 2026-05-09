package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardCartItemRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardCartResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class KimEduardCartController {

    private final KimEduardCartService cartService;

    @GetMapping
    public KimEduardCartResponseDTO getMyCart() {
        return cartService.getMyCart();
    }

    @PostMapping("/items")
    public KimEduardCartResponseDTO addItem(@Valid @RequestBody KimEduardCartItemRequestDTO dto) {
        return cartService.addItem(dto);
    }

    @PutMapping("/items/{productId}")
    public KimEduardCartResponseDTO updateItemQuantity(
            @PathVariable Long productId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") Integer quantity
    ) {
        return cartService.updateItemQuantity(productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public KimEduardCartResponseDTO removeItem(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }

    @DeleteMapping("/items")
    public void clearCart() {
        cartService.clearMyCart();
    }
}