package com.example.kimeduardfinalproject.dto.responses;

import com.example.kimeduardfinalproject.enums.KimEduardOrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KimEduardOrderResponseDTO {

    private Long id;
    private Long userId;
    private KimEduardOrderStatus status;
    private BigDecimal totalAmount;
    private List<KimEduardOrderItemResponseDTO> items;
    private LocalDateTime createdAt;
}