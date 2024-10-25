package com.fifth_semester.project.controllers;

import com.fifth_semester.project.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Tag(name="Book APIs")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/addBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> addNewBooks(@RequestParam String title,@RequestParam String author,@RequestParam String isbn,@RequestParam String publisher) {
        String result = bookService.addBook(title,author,isbn,publisher);
        if (result.contains("already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
//        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBooks(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String author,
                                         @RequestParam String isbn,
                                         @RequestParam(required = false) String publisher){
        String result = bookService.updateBook(isbn,title,author,publisher);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/removeBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> deleteBooks(@RequestParam String isbn){
        String result = bookService.deleteBook(isbn);
        return ResponseEntity.ok(result);
    }
}
