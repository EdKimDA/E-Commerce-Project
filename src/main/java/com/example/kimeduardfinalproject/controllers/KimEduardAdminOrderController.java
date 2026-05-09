package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardOrderResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class KimEduardAdminOrderController {

    private final KimEduardOrderService orderService;

    @GetMapping
    public List<KimEduardOrderResponseDTO> getAllOrders() {
        return orderService.getAllForAdmin();
    }

    @GetMapping("/{id}")
    public KimEduardOrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getByIdForAdmin(id);
    }
}