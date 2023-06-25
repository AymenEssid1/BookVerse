package com.aymen.security.book;


import com.aymen.security.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String author;
    private String description;
    private double price;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private Category category;




}
