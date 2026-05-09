package com.example.kimeduardfinalproject.services;

import com.example.kimeduardfinalproject.dto.requests.KimEduardAdminCreateUserRequestDTO;
import com.example.kimeduardfinalproject.dto.requests.KimEduardUserRegisterRequestDTO;
import com.example.kimeduardfinalproject.dto.requests.KimEduardUserUpdateRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardUserResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardCart;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.enums.KimEduardRole;
import com.example.kimeduardfinalproject.exceptions.KimEduardDuplicateEmailException;
import com.example.kimeduardfinalproject.exceptions.KimEduardDuplicateUsernameException;
import com.example.kimeduardfinalproject.exceptions.KimEduardUserNotFoundException;
import com.example.kimeduardfinalproject.mappers.KimEduardUserMapper;
import com.example.kimeduardfinalproject.repositories.KimEduardUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.kimeduardfinalproject.exceptions.KimEduardAccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kimeduardfinalproject.dto.requests.KimEduardLoginRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardLoginResponseDTO;
import com.example.kimeduardfinalproject.security.KimEduardJwtUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KimEduardUserService {
    private final KimEduardEmailService emailService;
    private final KimEduardUserRepository userRepository;
    private final KimEduardUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public KimEduardUserResponseDTO getCurrentUser() {
        return userMapper.toResponse(getCurrentUserEntity());
    }

    public KimEduardLoginResponseDTO login(KimEduardLoginRequestDTO dto) {
        KimEduardUser user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new KimEduardUserNotFoundException("Invalid username or password"));

        if (Boolean.TRUE.equals(user.getDeleted()) || !Boolean.TRUE.equals(user.getActive())) {
            throw new KimEduardAccessDeniedException("User account is disabled");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new KimEduardAccessDeniedException("Invalid username or password");
        }

        String token = KimEduardJwtUtil.generateToken(user.getUsername(), user.getRole());

        return new KimEduardLoginResponseDTO(
                token,
                user.getUsername(),
                user.getRole()
        );
    }

    @Transactional
    public KimEduardUserResponseDTO register(KimEduardUserRegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new KimEduardDuplicateEmailException("Email already exists");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new KimEduardDuplicateUsernameException("Username already exists");
        }

        KimEduardUser user = new KimEduardUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(KimEduardRole.USER);
        user.setActive(true);
        user.setDeleted(false);

        KimEduardCart cart = new KimEduardCart();
        cart.setUser(user);
        user.setCart(cart);

        KimEduardUser saved = userRepository.save(user);

        emailService.sendRegistrationEmail(saved.getEmail());

        return userMapper.toResponse(saved);
    }

    @Transactional
    public KimEduardUserResponseDTO adminCreateUser(KimEduardAdminCreateUserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new KimEduardDuplicateEmailException("Email already exists");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new KimEduardDuplicateUsernameException("Username already exists");
        }

        KimEduardUser user = new KimEduardUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setActive(true);
        user.setDeleted(false);

        KimEduardCart cart = new KimEduardCart();
        cart.setUser(user);
        user.setCart(cart);

        KimEduardUser saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    public List<KimEduardUserResponseDTO> getAll() {
        return userRepository.findByDeletedFalse()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public KimEduardUserResponseDTO getById(Long id) {
        KimEduardUser user = findEntityById(id);
        return userMapper.toResponse(user);
    }

    @Transactional
    public KimEduardUserResponseDTO update(Long id, KimEduardUserUpdateRequestDTO dto) {
        KimEduardUser user = findEntityById(id);

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new KimEduardDuplicateUsernameException("Username already exists");
            }
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new KimEduardDuplicateEmailException("Email already exists");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        KimEduardUser updated = userRepository.save(user);

        return userMapper.toResponse(updated);
    }

    @Transactional
    public void softDelete(Long id) {
        KimEduardUser user = findEntityById(id);

        user.setDeleted(true);
        user.setActive(false);

        userRepository.save(user);
    }

    public KimEduardUser getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new KimEduardAccessDeniedException("User is not authenticated");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new KimEduardUserNotFoundException("Current user not found"));
    }

    public KimEduardUser findEntityById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !Boolean.TRUE.equals(user.getDeleted()))
                .orElseThrow(() -> new KimEduardUserNotFoundException("User not found with id: " + id));
    }
}