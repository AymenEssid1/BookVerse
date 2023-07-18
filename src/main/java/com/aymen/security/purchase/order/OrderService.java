package com.aymen.security.purchase.order;


import com.aymen.security.book.Book;
import com.aymen.security.purchase.cart.Cart;
import com.aymen.security.purchase.cart.CartService;
import com.aymen.security.purchase.item.Item;
import com.aymen.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

@Autowired
   CartService cartService;
    @Autowired
    OrderRepository orderRepository;


    public List<Order> getOrders(Integer id){
       return orderRepository.findByUserId(id);


    }

    public Order createOrder(User user) {
        Cart cart = cartService.getCartByUser(user.getId());
        List<Item> cartItems = cart.getItems();
        Map<Book, Integer> itemMap = new HashMap<>();

        for (Item item : cartItems) {
            Book book = item.getBook();
            int quantity = item.getQuantity();
            itemMap.put(book, quantity);
        }
        System.out.println(itemMap);
        // Create the order
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .items(itemMap)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart


        return savedOrder;
    }

}
