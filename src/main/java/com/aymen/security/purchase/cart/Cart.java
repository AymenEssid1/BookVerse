package com.aymen.security.purchase.cart;

import com.aymen.security.book.Book;
import com.aymen.security.purchase.item.Item;
import com.aymen.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {


    @Id
    @GeneratedValue
    private Integer id;

    private double totalPrice;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @JsonIgnore
    @OneToOne(mappedBy = "cart")
    private User user;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart{");
        sb.append("id=").append(id);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", items=[");
        if (items != null) {
            for (Item item : items) {
                sb.append(item.getId()).append(", ");
            }
        }
        sb.append("]");
        sb.append('}');
        return sb.toString();
    }

}