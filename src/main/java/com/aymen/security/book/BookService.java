package com.aymen.security.book;


import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {


    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }


    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Integer id, Book updatedBook) {
        Optional<Book> existingBookOptional = bookRepository.findById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();

            String updatedName = updatedBook.getName();
            String oldName = existingBook.getName();

            // Check if the updated name is different from the old name
            if (!updatedName.equals(oldName)) {
                // Check if the updated name already exists in the database
                boolean isNameTaken = this.isBookNameExists(updatedName);

                if (isNameTaken) {
                    throw new RuntimeException("Book name already exists in the database");
                }
            }

            // Update the book properties
            existingBook.setName(updatedName);
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setDescription(updatedBook.getDescription());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setQuantity(updatedBook.getQuantity());
            existingBook.setCategory(updatedBook.getCategory());

            return bookRepository.save(existingBook);
        }
        return null;
    }


    public boolean deleteBook(Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            bookRepository.delete(bookOptional.get());
            return true;
        }
        return false;
    }

    public List<Book> searchBooks(String name, Category category, Double maxPrice) {
        Specification<Book> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(name)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")), name.toLowerCase()));
            }

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return bookRepository.findAll(specification);
    }


    public boolean isBookNameExists(String name) {
        Optional<Book> existingBook = this.findByNameIgnoreCase(name.trim());
        return existingBook.isPresent();
    }

    public Optional<Book> findByNameIgnoreCase(String searchName) {
        List<Book> allBooks = bookRepository.findAll();
        String modifiedSearchName = searchName.replaceAll("\\s", "").toLowerCase();

        for (Book book : allBooks) {
            String modifiedBookName = book.getName().replaceAll("\\s", "").toLowerCase();
            if (modifiedBookName.equals(modifiedSearchName)) {
                return Optional.of(book);
            }
        }

        return Optional.empty();
    }

    public void reduceQuant(Integer id, Integer value) {
       Book book = bookRepository.findById(id).orElse(null);
       book.setQuantity(book.getQuantity()-value);
       bookRepository.save(book);

    }

    public void increaseQuant(Integer id, Integer value) {
        Book book = bookRepository.findById(id).orElse(null);
        book.setQuantity(book.getQuantity()+value);
        bookRepository.save(book);
    }
    //this compares the  quantity  asked for compared to the
    public boolean checkQuant(Integer bookid ,Integer quant){
        Book book  = bookRepository.findById(bookid).orElse(null);
        int OGquant=book.getQuantity();
        if (quant<OGquant){return true;}
        return false;

    }
}
