package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KimEduardOrderItemRepository extends JpaRepository<KimEduardOrderItem, Long> {

    List<KimEduardOrderItem> findByOrderId(Long orderId);

    List<KimEduardOrderItem> findByProductId(Long productId);
}