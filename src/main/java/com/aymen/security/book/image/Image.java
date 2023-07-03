package com.aymen.security.book.image;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Entity
@Data
@ToString
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;



    public Image(String name, String location) {
        this.name = name;
        this.location = location;
    }



    public Image() {
    }
}
