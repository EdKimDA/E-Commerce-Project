package com.example.kimeduardfinalproject.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating or updating product")
public class KimEduardProductRequestDTO {
    @Schema(description = "Product name", example = "iPhone 15")
    @NotBlank(message = "Product name must not be empty")
    private String name;

    @Schema(description = "Product description", example = "Apple smartphone with 128GB storage")
    private String description;

    @Schema(description = "Product price", example = "499990.00")
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(description = "Available stock quantity", example = "10")
    @NotNull(message = "Stock quantity must not be null")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Schema(description = "Is product active", example = "true")
    private Boolean active;
}