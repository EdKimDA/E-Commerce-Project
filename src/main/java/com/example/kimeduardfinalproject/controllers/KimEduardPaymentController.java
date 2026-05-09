package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.requests.KimEduardPaymentRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardBalanceResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardPaymentResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/payments")
@RequiredArgsConstructor
public class KimEduardPaymentController {

    private final KimEduardPaymentService paymentService;

    @PostMapping("/top-up")
    public ResponseEntity<KimEduardPaymentResponseDTO> topUpBalance(@Valid @RequestBody KimEduardPaymentRequestDTO dto) {
        KimEduardPaymentResponseDTO response = paymentService.topUpBalance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<KimEduardPaymentResponseDTO> getMyPayments() {
        return paymentService.getMyPayments();
    }

    @GetMapping("/balance")
    public KimEduardBalanceResponseDTO getMyBalance() {
        return paymentService.getMyBalance();
    }
}