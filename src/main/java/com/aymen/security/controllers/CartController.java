package com.aymen.security.controllers;


import com.aymen.security.purchase.cart.Cart;
import com.aymen.security.purchase.cart.CartService;
import com.aymen.security.zchat.exceptions.UserNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    CartService cartService;


    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestParam int userId, @RequestParam int bookId) throws UserNotFoundException {
        try {
            Cart cart = cartService.addToCart(userId, bookId);
            return ResponseEntity.ok(cart);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/getCartByUser/{userid}")
    public ResponseEntity<Cart> getcart(@PathVariable Integer userid) throws UserNotFoundException{
        try{Cart cart=cartService.getCartByUser(userid);
            return ResponseEntity.ok(cart);}catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestParam("userId") Integer userId, @RequestParam("bookId") Integer bookId,@RequestParam("deletetype") int deletetype) throws UserNotFoundException{
        try {
            Cart cart = cartService.deleteFromCart(userId,bookId,deletetype);
            System.out.println("khedmet");
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            System.out.println("makhedmetch");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
