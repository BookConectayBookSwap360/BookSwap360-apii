package com.example.bookswap360.services;

import com.example.bookswap360.model.Book;
import com.example.bookswap360.model.BookExchange;
import com.example.bookswap360.model.User;
import com.example.bookswap360.repositories.BookExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BookExchangeService {

    private final BookExchangeRepository bookExchangeRepository;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookExchangeService(
            BookExchangeRepository bookExchangeRepository,
            BookService bookService,
            UserService userService
    ) {
        this.bookExchangeRepository = bookExchangeRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public BookExchange createBookExchange(BookExchange bookExchange) {
        // Obtén el libro y el usuario relacionados
        Book book = bookService.findById(bookExchange.getBook().getId());
        User sender = userService.findById(bookExchange.getSender().getId());

        // Verifica si el remitente es el propietario del libro
        if (!book.getOwnerName().equals(sender.getName())) {
            throw new IllegalArgumentException("El remitente no es el propietario del libro.");
        }

        // Continúa con la creación del intercambio si la validación pasa
        bookExchange.setCreatedAt(new Date());
        bookExchange.setStatus("pending");
        return bookExchangeRepository.save(bookExchange);
    }
}

