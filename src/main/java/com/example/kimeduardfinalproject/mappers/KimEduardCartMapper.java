package com.example.kimeduardfinalproject.mappers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardCartItemResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardCartResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardCart;
import com.example.kimeduardfinalproject.entities.KimEduardCartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class KimEduardCartMapper {
    public KimEduardCartItemResponseDTO toItemResponse(KimEduardCartItem item) {
        BigDecimal subtotal = item.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new KimEduardCartItemResponseDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                subtotal
        );
    }

    public KimEduardCartResponseDTO toResponse(KimEduardCart cart) {
        List<KimEduardCartItemResponseDTO> items = cart.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(KimEduardCartItemResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new KimEduardCartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                items,
                totalAmount,
                cart.getCreatedAt()
        );
    }
}