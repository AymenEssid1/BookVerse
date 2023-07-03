package com.aymen.security.book;


import com.aymen.security.book.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private Double averageReview;


    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;


}
