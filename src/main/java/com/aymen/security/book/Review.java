package com.aymen.security.book;

import com.aymen.security.user.User;
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
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer rating;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", user='" + user.getId() +
                ", book='" + book.getId() +
                ", rating='" + rating  +
                '}';
    }

}