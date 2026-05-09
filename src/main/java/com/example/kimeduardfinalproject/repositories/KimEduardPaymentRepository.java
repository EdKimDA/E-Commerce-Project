package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardPayment;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.enums.KimEduardPaymentStatus;
import com.example.kimeduardfinalproject.enums.KimEduardPaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KimEduardPaymentRepository extends JpaRepository<KimEduardPayment, Long> {

    List<KimEduardPayment> findByUser(KimEduardUser user);

    List<KimEduardPayment> findByUserId(Long userId);

    List<KimEduardPayment> findByUserIdAndStatus(Long userId, KimEduardPaymentStatus status);

    List<KimEduardPayment> findByUserIdAndTypeAndStatus(Long userId, KimEduardPaymentType type, KimEduardPaymentStatus status);

    Optional<KimEduardPayment> findByExternalTransactionId(String externalTransactionId);

    boolean existsByExternalTransactionId(String externalTransactionId);
}