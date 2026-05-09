package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardOrderResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class KimEduardOrderController {

    private final KimEduardOrderService orderService;

    @PostMapping("/create-from-cart")
    public ResponseEntity<KimEduardOrderResponseDTO> createOrderFromCart() {
        KimEduardOrderResponseDTO response = orderService.createOrderFromCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<KimEduardOrderResponseDTO> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/{id}")
    public KimEduardOrderResponseDTO getMyOrderById(@PathVariable Long id) {
        return orderService.getMyOrderById(id);
    }

    @PutMapping("/{id}/cancel")
    public KimEduardOrderResponseDTO cancelMyOrder(@PathVariable Long id) {
        return orderService.cancelMyOrder(id);
    }
}