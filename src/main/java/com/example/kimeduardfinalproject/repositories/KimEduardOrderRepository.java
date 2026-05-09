package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardOrder;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.enums.KimEduardOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KimEduardOrderRepository extends JpaRepository<KimEduardOrder, Long> {

    List<KimEduardOrder> findByUser(KimEduardUser user);

    List<KimEduardOrder> findByUserId(Long userId);

    List<KimEduardOrder> findByStatus(KimEduardOrderStatus status);

    List<KimEduardOrder> findByUserIdAndStatus(Long userId, KimEduardOrderStatus status);
}