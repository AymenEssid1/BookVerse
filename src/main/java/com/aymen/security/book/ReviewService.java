package com.aymen.security.book;

import com.aymen.security.user.User;
import com.aymen.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Book addReviewToBook(Integer bookId, Integer userId, Integer rating) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check if the user has already reviewed the book
        Optional<Review> existingReview = reviewRepository.findByBookIdAndUserId(bookId, userId);
        if (existingReview.isPresent()) {
            // Update the existing review
            Review review = existingReview.get();
            review.setRating(rating);
            reviewRepository.save(review);
        } else {
            // Create a new review
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Review newReview = new Review();
            newReview.setBook(book);
            newReview.setUser(user);
            newReview.setRating(rating);

            reviewRepository.save(newReview);
        }

        // Update the average review of the book
        updateAverageReview(book);

        double averageReview = book.getAverageReview();
        return book;
    }

    @Transactional
    void updateAverageReview(Book book) {
        List<Review> reviews = reviewRepository.findByBookId(book.getId());

        if (reviews.isEmpty()) {
            book.setAverageReview(0.0);
        } else {
            int totalRating = reviews.stream().mapToInt(Review::getRating).sum();
            double averageRating = (double) totalRating / reviews.size();
            book.setAverageReview(averageRating);
        }

        bookRepository.save(book);
    }

}