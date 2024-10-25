package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Book;
import com.fifth_semester.project.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fifth_semester.project.entities.BookStatus;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public String addBook(String title, String author, String isbn, String publisher) {
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isPresent()) {
            return "Book with ISBN " + bookOpt.get().getIsbn() + " already exists";
        }
        Book book = new Book(title,author,isbn,publisher,BookStatus.AVAILABLE);
        bookRepository.save(book);
        return "Book Saved Successfully";
    }

    public String updateBook(String isbn, String title, String author, String publisher) {
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (title != null && !title.isEmpty()) {
                book.setTitle(title);
            }
            if (author != null && !author.isEmpty()) {
                book.setAuthor(author);
            }
            if (publisher != null && !publisher.isEmpty()) {
                book.setPublisher(publisher);
            }
            bookRepository.save(book);
            return "Book Updated Successfully";
        }
        return "Book Not Found";
    }

    public String deleteBook(String isbn) {
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isPresent()) {
            bookRepository.delete(bookOpt.get());
            return "Book Deleted Successfully";
        }
        return "Book Not Found";
    }

}
