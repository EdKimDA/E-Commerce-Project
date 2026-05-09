package com.example.kimeduardfinalproject.repositories;

import com.example.kimeduardfinalproject.entities.User;
import com.example.kimeduardfinalproject.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByDeletedFalse();

    List<User> findByActiveTrueAndDeletedFalse();

    List<User> findByDeletedTrue();
}
