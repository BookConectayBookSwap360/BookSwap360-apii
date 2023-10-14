package com.example.bookswap360.repositories;

import com.example.bookswap360.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
