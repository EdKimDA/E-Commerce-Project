package com.example.kimeduardfinalproject.mappers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardOrderItemResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardOrderResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardOrder;
import com.example.kimeduardfinalproject.entities.KimEduardOrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KimEduardOrderMapper {
    public KimEduardOrderItemResponseDTO toItemResponse(KimEduardOrderItem item) {
        return new KimEduardOrderItemResponseDTO(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getPriceAtPurchase(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }

    public KimEduardOrderResponseDTO toResponse(KimEduardOrder order) {
        List<KimEduardOrderItemResponseDTO> items = order.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        return new KimEduardOrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                items,
                order.getCreatedAt()
        );
    }
}