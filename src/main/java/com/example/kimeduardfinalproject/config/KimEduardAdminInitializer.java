package com.example.kimeduardfinalproject.config;

import com.example.kimeduardfinalproject.entities.KimEduardCart;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.enums.KimEduardRole;
import com.example.kimeduardfinalproject.repositories.KimEduardUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class KimEduardAdminInitializer implements CommandLineRunner {

    private final KimEduardUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        KimEduardUser admin = new KimEduardUser();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(KimEduardRole.ADMIN);
        admin.setActive(true);
        admin.setDeleted(false);

        KimEduardCart cart = new KimEduardCart();
        cart.setUser(admin);
        admin.setCart(cart);

        userRepository.save(admin);

        System.out.println("Default admin created: username=admin, password=admin123");
    }
}