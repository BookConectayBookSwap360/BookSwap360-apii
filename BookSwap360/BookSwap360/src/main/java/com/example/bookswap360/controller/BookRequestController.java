package com.example.bookswap360.controller;

import com.example.bookswap360.exception.BookNotAvailableException;
import com.example.bookswap360.model.Book;
import com.example.bookswap360.model.BookRequest;
import com.example.bookswap360.model.User;
import com.example.bookswap360.repositories.BookRepository;
import com.example.bookswap360.repositories.BookRequestRepository;
import com.example.bookswap360.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class BookRequestController {

    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<BookRequest>> getAllBookRequests() {
        List<BookRequest> bookRequests = bookRequestRepository.findAll();
        return new ResponseEntity<>(bookRequests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequest> getBookRequestById(@PathVariable Long id) {
        BookRequest bookRequest = bookRequestRepository.findById(id).get();
        return new ResponseEntity<>(bookRequest, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookRequest> createBookRequest(@RequestBody BookRequest bookRequest) throws BookNotAvailableException {
        User requester = userRepository.findById(bookRequest.getRequester().getId()).orElse(null);
        Book requestedBook = bookRepository.findById(bookRequest.getRequestedBook().getId()).orElse(null);
        Book offeredBook = bookRepository.findById(bookRequest.getOfferedBook().getId()).orElse(null);

        if (requestedBook != null && "Disponible".equals(requestedBook.getStatus()) &&
                offeredBook != null && "Disponible".equals(offeredBook.getStatus()) &&
                requester != null && requester.equals(offeredBook.getOwner())) {

            bookRequest.setRequester(requester);
            bookRequest.setRequestedBook(requestedBook);
            bookRequest.setOfferedBook(offeredBook);
            bookRequestRepository.save(bookRequest);

            return new ResponseEntity<>(bookRequest, HttpStatus.CREATED);
        } else {
            throw new BookNotAvailableException("El libro solicitado u ofrecido no está disponible o no pertenece al solicitante");
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<BookRequest> updateBookRequest(@PathVariable Long id, @RequestBody BookRequest bookRequest) {
        BookRequest existingBookRequest = bookRequestRepository.findById(id).get();
        existingBookRequest.setStatus(bookRequest.getStatus());
        bookRequestRepository.save(existingBookRequest);
        return new ResponseEntity<>(existingBookRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookRequest(@PathVariable Long id) {
        bookRequestRepository.deleteById(id);
        return new ResponseEntity<>("Book request deleted successfully", HttpStatus.OK);
    }
}

