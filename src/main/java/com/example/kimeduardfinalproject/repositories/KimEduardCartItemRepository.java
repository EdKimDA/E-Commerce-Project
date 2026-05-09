package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardCart;
import com.example.kimeduardfinalproject.entities.KimEduardCartItem;
import com.example.kimeduardfinalproject.entities.KimEduardProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KimEduardCartItemRepository extends JpaRepository<KimEduardCartItem, Long> {

    List<KimEduardCartItem> findByCart(KimEduardCart cart);

    List<KimEduardCartItem> findByCartId(Long cartId);

    Optional<KimEduardCartItem> findByCartAndProduct(KimEduardCart cart, KimEduardProduct product);

    Optional<KimEduardCartItem> findByCartIdAndProductId(Long cartId, Long productId);

    void deleteByCartId(Long cartId);
}