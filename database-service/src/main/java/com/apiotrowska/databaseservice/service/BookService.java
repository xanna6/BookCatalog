package com.apiotrowska.databaseservice.service;

import com.apiotrowska.databaseservice.dto.BookRequest;
import com.apiotrowska.databaseservice.dto.BookResponse;
import com.apiotrowska.databaseservice.entity.Book;
import com.apiotrowska.databaseservice.exception.BookNotFoundException;
import com.apiotrowska.databaseservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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

    public List<BookResponse> getAllBooks(String[] sort) {
        List<Order> orders = parseSortParameterToListOfOrders(sort);
        System.out.println(orders);
        List<Book> books = this.bookRepository.findAll(Sort.by(orders));

        return books.stream().map(book -> mapBookToBookResponse(book)).toList();
    }

    public List<BookResponse> getAllBooksWithPagination(int offset, int pageSize, String[] sort) {
        List<Order> orders = parseSortParameterToListOfOrders(sort);
        Pageable paging = PageRequest.of(offset, pageSize, Sort.by(orders));
        System.out.println(orders);
        System.out.println(paging);
        Page<Book> books = this.bookRepository.findAll(paging);
        System.out.println(books);
        System.out.println(books.getContent());

        return books.getContent().stream().map(book -> mapBookToBookResponse(book)).toList();
    }

    public List<BookResponse> getFilteredBooks(String filter, String[] sort) {
        List<Order> orders = parseSortParameterToListOfOrders(sort);
        System.out.println(orders);
        List<Book> books = this.bookRepository.findByTitleContainingOrAuthorContaining(filter, filter, Sort.by(orders));

        return books.stream().map(book -> mapBookToBookResponse(book)).toList();
    }

    public List<BookResponse> getFilteredBooksWithPagination(int offset, int pageSize, String filter, String[] sort) {
        List<Order> orders = parseSortParameterToListOfOrders(sort);
        Pageable paging = PageRequest.of(offset, pageSize, Sort.by(orders));
        System.out.println(orders);
        System.out.println(paging);
        Page<Book> books = this.bookRepository.findByTitleContainingOrAuthorContaining(filter, filter, paging);
        System.out.println(books);
        System.out.println(books.getContent());

        return books.getContent().stream().map(book -> mapBookToBookResponse(book)).toList();
    }

    public BookResponse getBookById(Long id) throws BookNotFoundException {
        Optional<Book> bookData = this.bookRepository.findById(id);

        if (bookData.isPresent()) {
            return mapBookToBookResponse(bookData.get());
        } else {
            throw new BookNotFoundException("Not found Book with id = " + id);
        }
    }

    public BookResponse updateBook(Long id, BookRequest bookRequest) throws BookNotFoundException {
        Optional<Book> bookData = this.bookRepository.findById(id);

        if (bookData.isPresent()) {
            Book book = bookData.get();
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setPublicationYear(bookRequest.getPublicationYear());
            book.setPages(bookRequest.getPages());

            return mapBookToBookResponse(this.bookRepository.save(book));
        } else {
            throw new BookNotFoundException("Not found Book with id = " + id);
        }
    }

    public void deleteBook(Long id) {
        this.bookRepository.deleteById(id);
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

    private List<Order> parseSortParameterToListOfOrders(String[] sort) {
        List<Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sort="field,direction"
            for (String sortOrder : sort) {
                System.out.println(sortOrder);
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(getSortDirection(_sort[0], _sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            System.out.println(sort[0]);
            System.out.println(sort[1]);
            orders.add(new Order(getSortDirection(sort[0], sort[1]),
                    sort[0].equalsIgnoreCase("asc") || sort[0].equalsIgnoreCase("desc") ? sort[1] : sort[0]));
        }
        return orders;
    }

    private Direction getSortDirection(String s0, String s1) {
        if (s0.equalsIgnoreCase("desc")) {
            return Direction.DESC;
        } else if (s0.equalsIgnoreCase("asc")){
            return Direction.ASC;
        } else if (s1.equalsIgnoreCase("desc")) {
            return Direction.DESC;
        } else {
            return Direction.ASC;
        }
    }
}
