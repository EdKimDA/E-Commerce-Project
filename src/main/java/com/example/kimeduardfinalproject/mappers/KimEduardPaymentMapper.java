package com.example.kimeduardfinalproject.mappers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardPaymentResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardPayment;
import org.springframework.stereotype.Component;

@Component
public class KimEduardPaymentMapper {
    public KimEduardPaymentResponseDTO toResponse(KimEduardPayment payment) {
        Long orderId = payment.getOrder() != null
                ? payment.getOrder().getId()
                : null;

        return new KimEduardPaymentResponseDTO(
                payment.getId(),
                payment.getUser().getId(),
                orderId,
                payment.getAmount(),
                payment.getType(),
                payment.getStatus(),
                payment.getDescription(),
                payment.getExternalTransactionId(),
                payment.getCreatedAt()
        );
    }
}