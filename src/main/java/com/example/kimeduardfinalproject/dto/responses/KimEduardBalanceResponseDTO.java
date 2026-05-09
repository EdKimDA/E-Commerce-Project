package com.example.kimeduardfinalproject.dto.responses;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KimEduardBalanceResponseDTO {

    private Long userId;
    private BigDecimal balance;
}