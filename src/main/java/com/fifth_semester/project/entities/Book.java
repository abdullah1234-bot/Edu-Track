package com.fifth_semester.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "library_books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus availabilityStatus;  // Available, Checked out, etc.

    public Book() {}

    public Book(String title, String author, String isbn, BookStatus availabilityStatus) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
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
}
