package com.aymen.security.controllers;


import com.aymen.security.book.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;


    @Autowired
    private ReviewService reviewService;


    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("getbookby/{id}")
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read') ")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> optionalBook = bookService.getBookById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/Catalogue")
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read') ")
    public ResponseEntity<List<Book>> getAllBooks()  {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @PostMapping("/addBook")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }


    @PutMapping("/updateBook/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody Book updatedBook) {
        Book updated = bookService.updateBook(id, updatedBook);
        return updated != null
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/RemoveBook/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        boolean deleted = bookService.deleteBook(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read') ")
    public ResponseEntity<?> searchBooks(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Category category,
                                                  @RequestParam(required = false) Double maxPrice) {
        List<Book> books = bookService.searchBooks(name, category, maxPrice);

        if (books.isEmpty()) {
            return ResponseEntity.ok("no books were found");
        } else {
            return ResponseEntity.ok(books);
        }
    }

    @PostMapping("/{bookId}/reviews")
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read') ")
    public ResponseEntity<Book>  addReviewToBook(
            @PathVariable Integer bookId,
            @RequestParam Integer userId,
            @RequestParam Integer rating) {

        Book ch = reviewService.addReviewToBook(bookId, userId, rating);

        return ResponseEntity.ok(ch);
    }
}
