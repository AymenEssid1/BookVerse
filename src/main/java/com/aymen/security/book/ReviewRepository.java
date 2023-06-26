package com.aymen.security.book;

import com.aymen.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review, Integer> {
    Optional<Review> findByBookIdAndUserId(Integer bookId, Integer userId);
   List<Review> findByBookId(Integer bookId);
}
