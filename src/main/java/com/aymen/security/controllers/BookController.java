package com.aymen.security.controllers;


import com.aymen.security.book.*;
import com.aymen.security.book.image.IFileLocationService;
import com.aymen.security.book.image.Image;
import com.aymen.security.book.image.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;


    @Autowired
    private ReviewService reviewService;


    @Autowired
    IFileLocationService iFileLocationService;

    @Autowired
    private ImageRepository imageRepository;

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

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/Catalogue")
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read') ")
    public ResponseEntity<List<Book>> getAllBooks()  {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

/*
    @PostMapping(value = "/image" ,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Image> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            Image savedImageData = iFileLocationService.save(image);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
*/

    @PutMapping(value="/update-image/{bookId}",consumes = "multipart/form-data")
    public ResponseEntity<Image> updateImage(@PathVariable Integer bookId, @RequestBody MultipartFile file) {
        try {
            Book u =bookService.getBookById(bookId).orElse(null);///fix this shit later
            System.out.println(u.getImage().getId());
            long imageId=u.getImage().getId();

            Image updatedImage = iFileLocationService.update(imageId, file);
            return ResponseEntity.ok(updatedImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/image/{bookId}")
    public ResponseEntity<FileSystemResource> downloadImage(@PathVariable Integer bookId) {
        try {
            Book u =bookService.getBookById(bookId).orElse(null);///fix this shit later
           // System.out.println(u.getImage().getId());
            FileSystemResource fileSystemResource = iFileLocationService.find(u.getImage().getId());
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping(value = "/addBookV2", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Book> createBookV2(@RequestBody MultipartFile image,
                                             @RequestParam String name,
                                             @RequestParam String author,
                                             @RequestParam String description,
                                             @RequestParam double price,
                                             @RequestParam int quantity,
                                             @RequestParam Category category,
                                             @RequestParam double averageReview) {
        try {
            // Check if the book name already exists
            if (bookService.isBookNameExists(name)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return conflict status if the name already exists
            }

            // Save the image file
            Image savedImageData = iFileLocationService.save(image);

            // Set the image details in the book object
            Book book = new Book();
            book.setImage(savedImageData);
            book.setName(name);
            book.setAuthor(author);
            book.setDescription(description);
            book.setPrice(price);
            book.setQuantity(quantity);
            book.setCategory(category);
            book.setAverageReview(averageReview);

            // Create the book
            Book createdBook = bookService.createBook(book);

            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
