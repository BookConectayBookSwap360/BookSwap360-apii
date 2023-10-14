package com.example.bookswap360.controller;

import com.example.bookswap360.DTO.BookDTO;
import com.example.bookswap360.model.Book;
import com.example.bookswap360.model.User;
import com.example.bookswap360.repositories.BookRepository;
import com.example.bookswap360.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/libros")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOs = new ArrayList<>();

        for (Book book : books) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setAuthor(book.getAuthor());
            bookDTO.setOwnerId(book.getOwner().getId());
            bookDTO.setOwnerName(book.getOwner().getName());

            bookDTOs.add(bookDTO);
        }

        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id).get();
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // Busca al usuario (propietario) por su ID en la base de datos
        User user = userRepository.findById(book.getOwner().getId()).get();

        // Asigna al libro el propietario recuperado de la base de datos
        book.setOwner(user);

        // Guarda el libro en la base de datos
        bookRepository.save(book);

        // Devuelve una respuesta con el libro creado y un código de estado HTTP 201 (CREATED)
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book existingBook = bookRepository.findById(id).get();
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setCategory(book.getCategory());
        existingBook.setCondition(book.getCondition());
        existingBook.setPublishedDate(book.getPublishedDate());
        existingBook.setStatus(book.getStatus());
        bookRepository.save(existingBook);
        return new ResponseEntity<>(existingBook, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Book> updateBookStatus(@RequestBody Map<String, Object> updateInfo) {
        Long id = ((Number) updateInfo.get("id")).longValue();
        String newStatus = (String) updateInfo.get("status");

        Book existingBook = bookRepository.findById(id).orElse(null);

        if (existingBook != null) {
            existingBook.setStatus(newStatus);
            bookRepository.save(existingBook);
            return new ResponseEntity<>(existingBook, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve un estado 404 si el libro no se encuentra
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }
}
