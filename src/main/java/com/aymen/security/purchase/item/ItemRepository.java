package com.aymen.security.purchase.item;

import com.aymen.security.book.Book;
import com.aymen.security.purchase.cart.Cart;
import com.aymen.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Integer> {
    Item findByBookIdAndCart_Id(Integer bookId,Integer cart_Id);
}
