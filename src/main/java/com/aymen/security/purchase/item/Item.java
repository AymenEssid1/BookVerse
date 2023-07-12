package com.aymen.security.purchase.item;


import com.aymen.security.book.Book;
import com.aymen.security.purchase.cart.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue
    private Integer id;

    @JsonIgnore
    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Book book;

    private int quantity;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", book=" + book +
                ", quantity=" + quantity +
                '}';
    }

}
