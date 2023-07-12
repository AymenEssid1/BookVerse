package com.aymen.security.purchase.cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    public Cart getCartByUserId(Integer userId);
}
