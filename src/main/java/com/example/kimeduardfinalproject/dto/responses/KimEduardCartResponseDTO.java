package com.example.kimeduardfinalproject.dto.responses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KimEduardCartResponseDTO {

    private Long id;
    private Long userId;
    private List<KimEduardCartItemResponseDTO> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}