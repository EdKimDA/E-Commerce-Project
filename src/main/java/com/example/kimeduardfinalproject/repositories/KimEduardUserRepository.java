package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.enums.KimEduardRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KimEduardUserRepository extends JpaRepository<KimEduardUser, Long> {
    Optional<KimEduardUser> findByUsername(String username);

    Optional<KimEduardUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<KimEduardUser> findByRole(KimEduardRole role);

    List<KimEduardUser> findByDeletedFalse();

    List<KimEduardUser> findByActiveTrueAndDeletedFalse();

    List<KimEduardUser> findByDeletedTrue();
}
