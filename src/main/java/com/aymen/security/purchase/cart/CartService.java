package com.aymen.security.purchase.cart;


import com.aymen.security.book.Book;
import com.aymen.security.book.BookService;
import com.aymen.security.purchase.item.Item;
import com.aymen.security.purchase.item.ItemService;
import com.aymen.security.user.User;
import com.aymen.security.user.UserRepository;
import com.aymen.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class CartService {

    @Autowired
    UserService userService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    BookService bookService;
    @Autowired
    ItemService itemService;
    @Autowired
    UserRepository userRepository;


    public Cart getCartByUser(Integer userid) throws NotFoundException{

        Cart cart= cartRepository.getCartByUserId(userid);
        User user = userService.getUserById(userid);


        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
           return cartRepository.save(cart);

        }

        return cart;
    }

    public Cart addToCart(Integer userId, Integer bookId) throws NotFoundException {

        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId).orElse(null);
        if (user==null || book==null){
            throw new NotFoundException("not found");

        }
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
            cartRepository.save(cart);
        }
        itemService.addItem(book,cart);
        cart.setTotalPrice(cart.getTotalPrice()+book.getPrice());
        cartRepository.save(cart);

        return cart;
    }

    public Cart deleteFromCart (Integer userId, Integer bookId,int deletetype) throws NotFoundException {

        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId).orElse(null);
        if (user==null || book==null){
            throw new NotFoundException("not found");

        }
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
            cartRepository.save(cart);
        }
        itemService.removeItem(book,cart,deletetype);


        return cart;
    }


    public void emptyCart(Integer id) {
        User user = userService.getUserById(id);
        Cart cart =user.getCart();
        Cart newCart= new Cart();
        user.setCart(newCart);
        userRepository.save(user);
        cartRepository.delete(cart);



    }
}
