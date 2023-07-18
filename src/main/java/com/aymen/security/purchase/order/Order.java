package com.aymen.security.purchase.order;


import com.aymen.security.book.Book;
import com.aymen.security.purchase.item.Item;
import com.aymen.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;


    @JsonIgnore
    @ManyToOne
    private User user;


    private LocalDateTime orderDate;




    @ElementCollection
    private Map<Book, Integer> items;



    @Enumerated(EnumType.STRING)

    private PaymentStatus paymentStatus;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
               // ", user=" + user +
                ", orderDate=" + orderDate +
                '}';
    }


}
