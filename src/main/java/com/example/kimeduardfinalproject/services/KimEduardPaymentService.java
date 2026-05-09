package com.example.kimeduardfinalproject.services;

import com.example.kimeduardfinalproject.dto.requests.KimEduardPaymentRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardBalanceResponseDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardPaymentResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardOrder;
import com.example.kimeduardfinalproject.entities.KimEduardPayment;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.enums.KimEduardPaymentStatus;
import com.example.kimeduardfinalproject.enums.KimEduardPaymentType;
import com.example.kimeduardfinalproject.exceptions.KimEduardDuplicateTransactionException;
import com.example.kimeduardfinalproject.mappers.KimEduardPaymentMapper;
import com.example.kimeduardfinalproject.repositories.KimEduardPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KimEduardPaymentService {

    private final KimEduardPaymentRepository paymentRepository;
    private final KimEduardUserService userService;
    private final KimEduardPaymentMapper paymentMapper;

    public KimEduardPaymentResponseDTO topUpBalance(KimEduardPaymentRequestDTO dto) {
        KimEduardUser user = userService.getCurrentUserEntity();

        if (dto.getExternalTransactionId() != null && !dto.getExternalTransactionId().isBlank()) {
            if (paymentRepository.existsByExternalTransactionId(dto.getExternalTransactionId())) {
                throw new KimEduardDuplicateTransactionException("External transaction id already exists");
            }
        }

        KimEduardPayment payment = new KimEduardPayment();
        payment.setUser(user);
        payment.setAmount(dto.getAmount());
        payment.setType(KimEduardPaymentType.TOP_UP);
        payment.setStatus(KimEduardPaymentStatus.SUCCESS);
        payment.setDescription(dto.getDescription());
        payment.setExternalTransactionId(dto.getExternalTransactionId());

        KimEduardPayment saved = paymentRepository.save(payment);

        return paymentMapper.toResponse(saved);
    }

    public KimEduardPayment createOrderPayment(KimEduardUser user, KimEduardOrder order, BigDecimal amount) {
        KimEduardPayment payment = new KimEduardPayment();

        payment.setUser(user);
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setType(KimEduardPaymentType.ORDER_PAYMENT);
        payment.setStatus(KimEduardPaymentStatus.SUCCESS);
        payment.setDescription("Payment for order id: " + order.getId());

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public KimEduardBalanceResponseDTO getMyBalance() {
        KimEduardUser user = userService.getCurrentUserEntity();

        return new KimEduardBalanceResponseDTO(
                user.getId(),
                calculateBalance(user.getId())
        );
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long userId) {
        List<KimEduardPayment> successfulPayments =
                paymentRepository.findByUserIdAndStatus(userId, KimEduardPaymentStatus.SUCCESS);

        BigDecimal balance = BigDecimal.ZERO;

        for (KimEduardPayment payment : successfulPayments) {
            if (payment.getType() == KimEduardPaymentType.TOP_UP || payment.getType() == KimEduardPaymentType.REFUND) {
                balance = balance.add(payment.getAmount());
            }

            if (payment.getType() == KimEduardPaymentType.ORDER_PAYMENT) {
                balance = balance.subtract(payment.getAmount());
            }
        }

        return balance;
    }

    @Transactional(readOnly = true)
    public List<KimEduardPaymentResponseDTO> getMyPayments() {
        KimEduardUser user = userService.getCurrentUserEntity();

        return paymentRepository.findByUserId(user.getId())
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KimEduardPaymentResponseDTO> getUserPaymentsForAdmin(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}