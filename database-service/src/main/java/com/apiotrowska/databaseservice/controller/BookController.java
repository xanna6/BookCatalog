package com.apiotrowska.databaseservice.controller;

import com.apiotrowska.databaseservice.dto.BookRequest;
import com.apiotrowska.databaseservice.dto.BookResponse;
import com.apiotrowska.databaseservice.exception.BookNotFoundException;
import com.apiotrowska.databaseservice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/data")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooksWithPagination(@RequestParam(required = false) Integer offset,
                                                                        @RequestParam(required = false) Integer pageSize,
                                                                        @RequestParam(required = false) String filter,
                                                                        @RequestParam(defaultValue = "id,asc") String[] sort) {
        List<BookResponse> books;
        if (offset != null && pageSize != null) {
            if (filter != null && filter.length() != 0) {
                books = this.bookService.getFilteredBooksWithPagination(offset, pageSize, filter, sort);
            } else {
                books = this.bookService.getAllBooksWithPagination(offset, pageSize, sort);
            }
        } else {
            if (filter != null && filter.length() != 0) {
                books = this.bookService.getFilteredBooks(filter, sort);
            } else {
                books = this.bookService.getAllBooks(sort);
            }
        }
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) throws BookNotFoundException {
            BookResponse book = this.bookService.getBookById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = this.bookService.createBook(bookRequest);
        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) throws BookNotFoundException {
            BookResponse bookAfterUpdate = this.bookService.updateBook(id, bookRequest);
            return new ResponseEntity<>(bookAfterUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        this.bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
