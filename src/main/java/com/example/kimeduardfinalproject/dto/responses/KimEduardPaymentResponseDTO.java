package com.example.kimeduardfinalproject.dto.responses;

import com.example.kimeduardfinalproject.enums.KimEduardPaymentStatus;
import com.example.kimeduardfinalproject.enums.KimEduardPaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KimEduardPaymentResponseDTO {

    private Long id;
    private Long userId;
    private Long orderId;
    private BigDecimal amount;
    private KimEduardPaymentType type;
    private KimEduardPaymentStatus status;
    private String description;
    private String externalTransactionId;
    private LocalDateTime createdAt;
}