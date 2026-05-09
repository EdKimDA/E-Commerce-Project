package com.example.kimeduardfinalproject.services;


import com.example.kimeduardfinalproject.dto.requests.KimEduardProductRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardProductResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardProduct;
import com.example.kimeduardfinalproject.exceptions.KimEduardProductNotFoundException;
import com.example.kimeduardfinalproject.mappers.KimEduardProductMapper;
import com.example.kimeduardfinalproject.repositories.KimEduardProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KimEduardProductService {

    private final KimEduardProductRepository productRepository;
    private final KimEduardProductMapper productMapper;

    public KimEduardProductResponseDTO create(KimEduardProductRequestDTO dto) {
        KimEduardProduct product = new KimEduardProduct();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setActive(dto.getActive() != null ? dto.getActive() : true);
        product.setDeleted(false);

        KimEduardProduct saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<KimEduardProductResponseDTO> getAllVisible() {
        return productRepository.findByActiveTrueAndDeletedFalse()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KimEduardProductResponseDTO> getAllForAdmin() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public KimEduardProductResponseDTO getById(Long id) {
        return productMapper.toResponse(findActiveProductById(id));
    }

    @Transactional(readOnly = true)
    public List<KimEduardProductResponseDTO> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name)
                .stream()
                .filter(product -> Boolean.TRUE.equals(product.getActive()))
                .map(productMapper::toResponse)
                .toList();
    }

    public KimEduardProductResponseDTO update(Long id, KimEduardProductRequestDTO dto) {
        KimEduardProduct product = findEntityById(id);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());

        if (dto.getActive() != null) {
            product.setActive(dto.getActive());
        }

        KimEduardProduct updated = productRepository.save(product);

        return productMapper.toResponse(updated);
    }

    public void softDelete(Long id) {
        KimEduardProduct product = findEntityById(id);

        product.setDeleted(true);
        product.setActive(false);

        productRepository.save(product);
    }

    public KimEduardProduct findEntityById(Long id) {
        return productRepository.findById(id)
                .filter(product -> !Boolean.TRUE.equals(product.getDeleted()))
                .orElseThrow(() -> new KimEduardProductNotFoundException("Product not found with id: " + id));
    }

    public KimEduardProduct findActiveProductById(Long id) {
        KimEduardProduct product = findEntityById(id);

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new KimEduardProductNotFoundException("Product is not active with id: " + id);
        }

        return product;
    }
}