package com.fifth_semester.project.controllers;

import com.fifth_semester.project.entities.Book;
import com.fifth_semester.project.services.LibraryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@Tag(name = "Library APIs")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    // Search for books by title, author, or ISBN
    @GetMapping("/search")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        List<Book> books = libraryService.searchBooks(query);
        return ResponseEntity.ok(books);
    }

    // Check if a book is available
    @GetMapping("/check-availability/{bookId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Boolean> checkBookAvailability(@PathVariable Long bookId) {
        boolean isAvailable = libraryService.isBookAvailable(bookId);
        return ResponseEntity.ok(isAvailable);
    }

    // Reserve a book
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> reserveBook(@RequestParam Long bookId, @RequestParam Long studentId) {
        String result = libraryService.reserveBook(bookId, studentId);
        return ResponseEntity.ok(result);
    }

    // Borrow a book
    @PostMapping("/borrow")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> borrowBook(@RequestParam Long bookId, @RequestParam Long studentId) {
        String result = libraryService.borrowBook(bookId, studentId);
        return ResponseEntity.ok(result);
    }

    // Return a book
    @PostMapping("/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> returnBook(@RequestParam Long bookId, @RequestParam Long studentId) {
        String result = libraryService.returnBook(bookId, studentId);
        return ResponseEntity.ok(result);
    }

    // Apply overdue penalties for a student
    @PostMapping("/apply-penalties")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> applyOverduePenalties(@RequestParam Long studentId) {
        String result = libraryService.applyOverduePenalties(studentId);
        return ResponseEntity.ok(result);
    }
}
