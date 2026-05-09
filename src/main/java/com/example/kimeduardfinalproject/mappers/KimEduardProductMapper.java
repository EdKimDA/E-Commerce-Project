package com.example.kimeduardfinalproject.mappers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardProductResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardProduct;
import org.springframework.stereotype.Component;

@Component
public class KimEduardProductMapper {
    public KimEduardProductResponseDTO toResponse(KimEduardProduct product) {
        return new KimEduardProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getActive(),
                product.getDeleted(),
                product.getCreatedAt()
        );
    }
}