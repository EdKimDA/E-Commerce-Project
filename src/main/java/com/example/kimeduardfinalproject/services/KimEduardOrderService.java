package com.example.kimeduardfinalproject.services;


import com.example.kimeduardfinalproject.dto.responses.KimEduardOrderResponseDTO;
import com.example.kimeduardfinalproject.entities.*;
import com.example.kimeduardfinalproject.enums.KimEduardOrderStatus;
import com.example.kimeduardfinalproject.exceptions.KimEduardEmptyCartException;
import com.example.kimeduardfinalproject.exceptions.KimEduardInsufficientBalanceException;
import com.example.kimeduardfinalproject.exceptions.KimEduardInsufficientStockException;
import com.example.kimeduardfinalproject.exceptions.KimEduardOrderNotFoundException;
import com.example.kimeduardfinalproject.mappers.KimEduardOrderMapper;
import com.example.kimeduardfinalproject.repositories.KimEduardCartRepository;
import com.example.kimeduardfinalproject.repositories.KimEduardOrderRepository;
import com.example.kimeduardfinalproject.repositories.KimEduardProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kimeduardfinalproject.exceptions.KimEduardAccessDeniedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KimEduardOrderService {

    private final KimEduardOrderRepository orderRepository;
    private final KimEduardCartService cartService;
    private final KimEduardUserService userService;
    private final KimEduardPaymentService paymentService;
    private final KimEduardProductRepository productRepository;
    private final KimEduardCartRepository cartRepository;
    private final KimEduardOrderMapper orderMapper;

    public KimEduardOrderResponseDTO createOrderFromCart() {
        KimEduardUser user = userService.getCurrentUserEntity();
        KimEduardCart cart = cartService.getCartByUser(user);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new KimEduardEmptyCartException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (KimEduardCartItem cartItem : cart.getItems()) {
            KimEduardProduct product = cartItem.getProduct();

            if (Boolean.TRUE.equals(product.getDeleted()) || !Boolean.TRUE.equals(product.getActive())) {
                throw new KimEduardInsufficientStockException("Product is not available: " + product.getName());
            }

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new KimEduardInsufficientStockException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(subtotal);
        }

        BigDecimal balance = paymentService.calculateBalance(user.getId());

        if (balance.compareTo(totalAmount) < 0) {
            throw new KimEduardInsufficientBalanceException("Insufficient balance");
        }

        KimEduardOrder order = new KimEduardOrder();
        order.setUser(user);
        order.setStatus(KimEduardOrderStatus.PAID);
        order.setTotalAmount(totalAmount);

        List<KimEduardOrderItem> orderItems = new ArrayList<>();

        for (KimEduardCartItem cartItem : cart.getItems()) {
            KimEduardProduct product = cartItem.getProduct();

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            KimEduardOrderItem orderItem = new KimEduardOrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setItems(orderItems);

        KimEduardOrder savedOrder = orderRepository.save(order);

        paymentService.createOrderPayment(user, savedOrder, totalAmount);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<KimEduardOrderResponseDTO> getMyOrders() {
        KimEduardUser user = userService.getCurrentUserEntity();

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public KimEduardOrderResponseDTO getMyOrderById(Long orderId) {
        KimEduardUser user = userService.getCurrentUserEntity();

        KimEduardOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new KimEduardOrderNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new KimEduardAccessDeniedException("You cannot access this order");
        }

        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<KimEduardOrderResponseDTO> getAllForAdmin() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public KimEduardOrderResponseDTO getByIdForAdmin(Long id) {
        KimEduardOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new KimEduardOrderNotFoundException("Order not found with id: " + id));

        return orderMapper.toResponse(order);
    }

    public KimEduardOrderResponseDTO cancelMyOrder(Long id) {
        KimEduardUser user = userService.getCurrentUserEntity();

        KimEduardOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new KimEduardOrderNotFoundException("Order not found with id: " + id));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new KimEduardAccessDeniedException("You cannot cancel this order");
        }

        if (order.getStatus() == KimEduardOrderStatus.CANCELLED) {
            return orderMapper.toResponse(order);
        }

        order.setStatus(KimEduardOrderStatus.CANCELLED);

        KimEduardOrder updated = orderRepository.save(order);

        return orderMapper.toResponse(updated);
    }
}