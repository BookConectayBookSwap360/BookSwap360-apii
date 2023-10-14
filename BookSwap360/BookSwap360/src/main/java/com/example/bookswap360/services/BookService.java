package com.example.bookswap360.services;

import com.example.bookswap360.model.Book;
import com.example.bookswap360.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> obtenerTodosLosLibros() {
        return bookRepository.findAll(); // Obtener todos los libros almacenados en la base de datos
    }

    public Book findById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get(); // Obtener un libro por su ID
        } else {
            throw new IllegalArgumentException("El libro con ID " + id + " no se encontró.");
        }
    }

    public Book guardarLibro(Book libro) {
        // Aquí puedes realizar validaciones o lógica de negocio antes de guardar el libro
        return bookRepository.save(libro); // Guardar el libro en la base de datos
    }

    public void eliminarLibro(Long id) {
        bookRepository.deleteById(id); // Eliminar un libro por su ID
    }
}
