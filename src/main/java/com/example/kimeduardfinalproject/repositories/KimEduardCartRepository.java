package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardCart;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KimEduardCartRepository extends JpaRepository<KimEduardCart, Long> {

    Optional<KimEduardCart> findByUser(KimEduardUser user);

    Optional<KimEduardCart> findByUserId(Long userId);
}