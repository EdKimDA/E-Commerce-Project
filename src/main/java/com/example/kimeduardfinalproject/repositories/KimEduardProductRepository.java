package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KimEduardProductRepository extends JpaRepository<KimEduardProduct, Long> {

    List<KimEduardProduct> findByDeletedFalse();

    List<KimEduardProduct> findByActiveTrueAndDeletedFalse();

    List<KimEduardProduct> findByNameContainingIgnoreCaseAndDeletedFalse(String name);
}