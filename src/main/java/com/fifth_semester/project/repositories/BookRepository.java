package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search books by title, author, or ISBN
    List<Book> findByTitleContainingOrAuthorContainingOrIsbnContaining(String title, String author, String isbn);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingOrAuthorContaining(String query, String query1);
}
