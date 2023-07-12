package com.aymen.security.purchase.item;



import com.aymen.security.book.Book;
import com.aymen.security.purchase.cart.Cart;
import com.aymen.security.purchase.cart.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepository repo;
    @Autowired
    CartRepository cartRepository;

    public void removeItem(Book book, Cart cart ,int deleteType) { //0 is delete all instances 1 is -1 from quant

        Item existingItem =repo.findByBookIdAndCart_Id(book.getId(), cart.getId());
        Item newItem =new Item();
        if (existingItem!=null) // if item with that book exists
        {   if(deleteType==1){
                if(existingItem.getQuantity()>1)
                    {existingItem.setQuantity(existingItem.getQuantity()-1); repo.save(existingItem);
                    cart.setTotalPrice(cart.getTotalPrice()-book.getPrice());
                    cartRepository.save(cart);}
                else {  cart.setTotalPrice(cart.getTotalPrice()-book.getPrice());
                    repo.delete(existingItem);}}
            if (deleteType == 0) {

                cart.setTotalPrice(cart.getTotalPrice()- existingItem.getQuantity()* book.getPrice());
                repo.delete(existingItem);
                cartRepository.save(cart);
            }
        }

    }

    public Item addItem(Book book, Cart cart) {

        Item existingItem =repo.findByBookIdAndCart_Id(book.getId(), cart.getId());
        Item newItem =new Item();
        if (existingItem!=null) // if item with that book exists
        {
            existingItem.setQuantity(existingItem.getQuantity()+1) ;
           return repo.save(existingItem);

        }

        else {
            newItem.setBook(book);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            return repo.save(newItem);
        }
    }

















}
