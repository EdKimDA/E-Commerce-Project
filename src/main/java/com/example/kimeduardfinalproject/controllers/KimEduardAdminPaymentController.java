package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardPaymentResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class KimEduardAdminPaymentController {

    private final KimEduardPaymentService paymentService;

    @GetMapping("/users/{userId}")
    public List<KimEduardPaymentResponseDTO> getUserPayments(@PathVariable Long userId) {
        return paymentService.getUserPaymentsForAdmin(userId);
    }
}