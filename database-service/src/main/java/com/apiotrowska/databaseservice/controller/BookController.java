package com.apiotrowska.databaseservice.controller;

import com.apiotrowska.common.dto.BookFilterDto;
import com.apiotrowska.common.dto.BookRequest;
import com.apiotrowska.common.dto.BookResponse;
import com.apiotrowska.databaseservice.exception.BookNotFoundException;
import com.apiotrowska.databaseservice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/data")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(Pageable pageable,
                                                          @ModelAttribute BookFilterDto bookFilterDto) {

        Page<BookResponse> books = this.bookService.getBooks(bookFilterDto.getBookFilters(), pageable);

        if (books.getContent().isEmpty()) {
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/" + bookResponse.getId())
                .body(bookResponse);
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
