package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByDeletedFalse();

    List<Product> findByActiveTrueAndDeletedFalse();

    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name);
}