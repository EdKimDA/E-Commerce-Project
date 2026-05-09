package com.example.kimeduardfinalproject.services;

import com.example.kimeduardfinalproject.entities.KimEduardOrder;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KimEduardEmailService {

    private final JavaMailSender mailSender;

    @Async
    public CompletableFuture<Void> sendRegistrationEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Registration successful");
            message.setText("You have successfully registered.");

            mailSender.send(message);

            log.info("Registration email sent to {}", toEmail);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("Failed to send registration email to {}", toEmail, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Void> sendOrderSuccessEmail(KimEduardUser user, KimEduardOrder order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Order created");
            message.setText(
                    "Hello, " + user.getUsername() +
                            "! Your order #" + order.getId() +
                            " was successfully created."
            );

            mailSender.send(message);

            log.info("Order email sent to {}", user.getEmail());

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("Failed to send order email to {}", user.getEmail(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
}