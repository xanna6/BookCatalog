package com.apiotrowska.databaseservice.service;

import com.apiotrowska.databaseservice.dto.BookRequest;
import com.apiotrowska.databaseservice.dto.BookResponse;
import com.apiotrowska.databaseservice.entity.Book;
import com.apiotrowska.databaseservice.exception.BookNotFoundException;
import com.apiotrowska.databaseservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse createBook(BookRequest bookRequest) {
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .publicationYear(bookRequest.getPublicationYear())
                .pages(bookRequest.getPages())
                .build();

        Book savedBook = this.bookRepository.save(book);
        return mapBookToBookResponse(savedBook);
    }

    public Page<BookResponse> getBooks(Pageable pageable, String filter) {
        log.info(String.valueOf(pageable));
        Page<Book> books = this.bookRepository.findByTitleContainingOrAuthorContaining(filter, filter, pageable);

        return books.map(book -> mapBookToBookResponse(book));
    }

    public BookResponse getBookById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .map(book -> mapBookToBookResponse(book))
                .orElseThrow(() -> new BookNotFoundException("Not found Book with id = " + id));
    }

    public BookResponse updateBook(Long id, BookRequest bookRequest) throws BookNotFoundException {
        return bookRepository.findById(id)
                .map(book -> setFieldsAndUpdateBookInDatabase(book, bookRequest))
                .orElseThrow(() -> new BookNotFoundException("Not found Book with id = " + id));
    }

    public void deleteBook(Long id) {
        this.bookRepository.deleteById(id);
    }

    private BookResponse setFieldsAndUpdateBookInDatabase(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setPages(bookRequest.getPages());
        return mapBookToBookResponse(this.bookRepository.save(book));
    }

    private BookResponse mapBookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publicationYear(book.getPublicationYear())
                .pages(book.getPages())
                .build();
    }
}
