package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import javax.validation.constraints.Size;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String title;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String publisher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus availabilityStatus;  // Available, Checked out, etc.

    public Book() {}

    public Book(String title, String author, String isbn,String publisher, BookStatus availabilityStatus) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BookStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(BookStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
