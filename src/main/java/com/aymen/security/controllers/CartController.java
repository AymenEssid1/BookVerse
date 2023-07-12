package com.aymen.security.controllers;


import com.aymen.security.purchase.cart.Cart;
import com.aymen.security.purchase.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    CartService cartService;
    @GetMapping("/getCartByUser/{userid}")
    public ResponseEntity<Cart> getcart(@PathVariable Integer userid){
        try{Cart cart=cartService.getCartByUser(userid);
        return ResponseEntity.ok(cart);}catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestParam("userId") Integer userId, @RequestParam("bookId") Integer bookId) {
        try {
            cartService.addToCart(userId, bookId);
            return ResponseEntity.ok("Book added to cart successfully.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add book to cart.");
        }
    }


    @PostMapping("/removeFromCart")
    public ResponseEntity<String> removeFromCart(@RequestParam("userId") Integer userId, @RequestParam("bookId") Integer bookId,@RequestParam("deletetype") int deletetype) {
        try {
            cartService.deleteFromCart(userId,bookId,deletetype);
            return ResponseEntity.ok( "good");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("lol u thought");
        }
    }
}
