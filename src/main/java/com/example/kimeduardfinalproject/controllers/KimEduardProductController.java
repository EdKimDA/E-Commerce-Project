package com.example.kimeduardfinalproject.controllers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardPageResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardProductResponseDTO;
import com.example.kimeduardfinalproject.services.KimEduardProductService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import com.example.kimeduardfinalproject.entities.KimEduardProduct;
import com.example.kimeduardfinalproject.services.KimEduardFileStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Tag(
        name = "Products",
        description = "Public product endpoints"
)
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class KimEduardProductController {
    private final KimEduardFileStorageService fileStorageService;
    private final KimEduardProductService productService;

    @Operation(
            summary = "Get all visible products with pagination and sorting",
            description = "Returns active and not deleted products with page, size, sortBy and direction parameters."
    )
    @ApiResponse(responseCode = "200", description = "Products successfully returned")
    @GetMapping
    public KimEduardPageResponseDTO<KimEduardProductResponseDTO> getAllVisible(
            @Parameter(description = "Page number, starts from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Field for sorting", example = "price")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: asc or desc", example = "asc")
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return productService.getAllVisible(page, size, sortBy, direction);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Returns one active product by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public KimEduardProductResponseDTO getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(
            summary = "Search products by name",
            description = "Searches active products by partial product name."
    )
    @ApiResponse(responseCode = "200", description = "Search completed")
    @GetMapping("/search")
    public List<KimEduardProductResponseDTO> search(@RequestParam String name) {
        return productService.searchByName(name);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> downloadProductImage(@PathVariable Long id) {
        KimEduardProduct product = productService.getProductForImage(id);

        if (product.getImageFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = fileStorageService.loadFile(product.getImageFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + product.getImageFileName() + "\""
                )
                .body(resource);
    }
}