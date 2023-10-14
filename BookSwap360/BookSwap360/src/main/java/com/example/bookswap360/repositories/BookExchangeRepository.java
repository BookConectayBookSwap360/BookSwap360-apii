package com.example.bookswap360.repositories;

import com.example.bookswap360.model.BookExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookExchangeRepository extends JpaRepository<BookExchange, Long> {
}

