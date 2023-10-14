package com.example.bookswap360.controller;

import com.example.bookswap360.model.Book;
import com.example.bookswap360.model.BookExchange;
import com.example.bookswap360.model.User;
import com.example.bookswap360.repositories.BookExchangeRepository;
import com.example.bookswap360.repositories.BookRepository;
import com.example.bookswap360.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/intercambios")
public class BookExchangeController {

    @Autowired
    private BookExchangeRepository bookExchangeRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<BookExchange>> getAllBookExchanges() {
        List<BookExchange> bookExchanges = bookExchangeRepository.findAll();
        return new ResponseEntity<>(bookExchanges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookExchange> getBookExchangeById(@PathVariable Long id) {
        BookExchange bookExchange = bookExchangeRepository.findById(id).get();
        return new ResponseEntity<>(bookExchange, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createBookExchange(@RequestBody BookExchange bookExchange) {
        // Obtén el libro y el usuario relacionados
        User sender = userRepository.findById(bookExchange.getSender().getId()).orElse(null);
        User receiver = userRepository.findById(bookExchange.getReceiver().getId()).orElse(null);
        Book book = bookRepository.findById(bookExchange.getBook().getId()).orElse(null);

        // Verifica si el remitente y el libro existen
        if (sender == null || book == null) {
            return new ResponseEntity<>("El remitente o el libro no existen.", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el remitente tiene el mismo ID que el propietario del libro
        if (!book.getOwner().getId().equals(sender.getId())) {
            return new ResponseEntity<>("El remitente no es el propietario del libro.", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el receptor existe
        if (receiver == null) {
            return new ResponseEntity<>("El receptor no existe.", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el libro ya ha sido intercambiado
        if ("Intercambiado".equals(book.getStatus())) {
            return new ResponseEntity<>("El libro ya ha sido intercambiado.", HttpStatus.BAD_REQUEST);
        }

        // Continúa con la creación del intercambio si la validación pasa
        bookExchange.setReceiver(receiver);
        bookExchange.setSender(sender);
        bookExchange.setBook(book);
        bookExchange.setStatus("Aceptado");
        bookExchangeRepository.save(bookExchange);

        // Cambia el estado del libro a "Intercambiado"
        book.setStatus("Intercambiado");
        bookRepository.save(book);

        // Send notification to receiver
        return new ResponseEntity<>(bookExchange, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<BookExchange> updateBookExchange(@PathVariable Long id, @RequestBody BookExchange bookExchange) {
        BookExchange existingBookExchange = bookExchangeRepository.findById(id).get();
        existingBookExchange.setStatus(bookExchange.getStatus());
        bookExchangeRepository.save(existingBookExchange);
        return new ResponseEntity<>(existingBookExchange, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookExchange(@PathVariable Long id) {
        bookExchangeRepository.deleteById(id);
        return new ResponseEntity<>("Book exchange deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<BookExchange> acceptBookExchange(@PathVariable Long id) {
        BookExchange bookExchange = bookExchangeRepository.findById(id).get();
        bookExchange.setStatus("accepted");
        bookExchangeRepository.save(bookExchange);
        // Send notification to sender
        return new ResponseEntity<>(bookExchange, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<BookExchange> rejectBookExchange(@PathVariable Long id) {
        BookExchange bookExchange = bookExchangeRepository.findById(id).get();
        bookExchange.setStatus("rejected");
        bookExchangeRepository.save(bookExchange);
        // Send notification to sender
        return new ResponseEntity<>(bookExchange, HttpStatus.OK);
    }
}

