package com.aymen.security.book;

import com.aymen.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Integer> , JpaSpecificationExecutor<Book> {
}
