package com.example.kimeduardfinalproject.entities;


import com.example.kimeduardfinalproject.enums.KimEduardPaymentStatus;
import com.example.kimeduardfinalproject.enums.KimEduardPaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class KimEduardPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private KimEduardUser user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private KimEduardOrder order;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private KimEduardPaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private KimEduardPaymentStatus status = KimEduardPaymentStatus.PENDING;

    @Column(length = 1000)
    private String description;

    @Column(unique = true)
    private String externalTransactionId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = KimEduardPaymentStatus.PENDING;
        }
    }
}