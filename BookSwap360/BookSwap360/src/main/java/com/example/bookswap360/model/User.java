package com.example.bookswap360.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    private List<Book> books;

    /*@OneToMany(mappedBy = "requester")
    @JsonBackReference
    private List<BookRequest> bookRequests;

    @OneToMany(mappedBy = "sender")
    private List<BookExchange> bookExchanges;*/
}

