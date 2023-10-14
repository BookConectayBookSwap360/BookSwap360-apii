package com.example.bookswap360.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private String category;
    private String condition;
    private Date publishedDate;
    @Getter
    private String status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private User owner;

    public String getOwnerName() {
        if (owner != null) {
            return owner.getName();
        }
        return null;
    }
    public Long getOwnerId() {
        if (owner != null) {
            return owner.getId();
        }
        return null;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
