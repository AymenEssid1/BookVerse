package com.aymen.security.controllers;

import com.aymen.security.purchase.order.Order;
import com.aymen.security.purchase.order.OrderResponse;
import com.aymen.security.purchase.order.OrderService;
import com.aymen.security.user.User;
import com.aymen.security.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/OrderHistory")
    public ResponseEntity<List<Order>> orderHistory(@RequestParam Integer id){

        List<Order>lista= orderService.getOrders(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(lista);

    }
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestParam Integer id) {
        // Get the authenticated user
        User user = userService.getUserById(id);

        // Create the order
        OrderResponse order = orderService.createOrder(user);

        // Return a success response
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}