package com.aymen.security.controllers;


import com.aymen.security.book.BookService;
import com.aymen.security.book.ReviewService;
import com.aymen.security.book.image.IFileLocationService;
import com.aymen.security.book.image.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/image")
public class ImageController {
    private final BookService bookService;


    @Autowired
    private ReviewService reviewService;


    @Autowired
    IFileLocationService iFileLocationService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    public ImageController(BookService bookService) {
        this.bookService = bookService;
    }

}
