package com.example.kimeduardfinalproject.services;

import com.example.kimeduardfinalproject.dto.requests.KimEduardCartItemRequestDTO;
import com.example.kimeduardfinalproject.dto.responses.KimEduardCartResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardCart;
import com.example.kimeduardfinalproject.entities.KimEduardCartItem;
import com.example.kimeduardfinalproject.entities.KimEduardProduct;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import com.example.kimeduardfinalproject.exceptions.KimEduardCartItemNotFoundException;
import com.example.kimeduardfinalproject.exceptions.KimEduardCartNotFoundException;
import com.example.kimeduardfinalproject.mappers.KimEduardCartMapper;
import com.example.kimeduardfinalproject.repositories.KimEduardCartItemRepository;
import com.example.kimeduardfinalproject.repositories.KimEduardCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Transactional
public class KimEduardCartService {

    private final KimEduardCartRepository cartRepository;
    private final KimEduardCartItemRepository cartItemRepository;
    private final KimEduardProductService productService;
    private final KimEduardUserService userService;
    private final KimEduardCartMapper cartMapper;

    @Transactional(readOnly = true)
    public KimEduardCartResponseDTO getMyCart() {
        KimEduardUser user = userService.getCurrentUserEntity();
        KimEduardCart cart = getCartByUser(user);

        return cartMapper.toResponse(cart);
    }

    public KimEduardCartResponseDTO addItem(KimEduardCartItemRequestDTO dto) {
        KimEduardUser user = userService.getCurrentUserEntity();
        KimEduardCart cart = getCartByUser(user);
        KimEduardProduct product = productService.findActiveProductById(dto.getProductId());

        KimEduardCartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (item == null) {
            item = new KimEduardCartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());

            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + dto.getQuantity());
        }

        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    public KimEduardCartResponseDTO updateItemQuantity(Long productId, Integer quantity) {
        KimEduardUser user = userService.getCurrentUserEntity();
        KimEduardCart cart = getCartByUser(user);

        KimEduardCartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new KimEduardCartItemNotFoundException("Cart item not found"));

        item.setQuantity(quantity);

        cartItemRepository.save(item);

        return cartMapper.toResponse(cart);
    }

    public KimEduardCartResponseDTO removeItem(Long productId) {
        KimEduardUser user = userService.getCurrentUserEntity();
        KimEduardCart cart = getCartByUser(user);

        boolean removed = false;

        Iterator<KimEduardCartItem> iterator = cart.getItems().iterator();

        while (iterator.hasNext()) {
            KimEduardCartItem item = iterator.next();

            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                item.setCart(null);
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new KimEduardCartItemNotFoundException("Cart item not found");
        }

        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }

    public void clearMyCart() {
        KimEduardUser user = userService.getCurrentUserEntity();
        KimEduardCart cart = getCartByUser(user);

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    public KimEduardCart getCartByUser(KimEduardUser user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new KimEduardCartNotFoundException("Cart not found for user id: " + user.getId()));
    }
}