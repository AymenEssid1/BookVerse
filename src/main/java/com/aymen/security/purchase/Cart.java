package com.aymen.security.purchase;

import com.aymen.security.book.Book;
import com.aymen.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Integer id;

   /* @OneToOne(mappedBy = "cart")
    private User user;*/


}