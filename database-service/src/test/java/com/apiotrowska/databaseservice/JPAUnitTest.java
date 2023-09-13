package com.apiotrowska.databaseservice;

import com.apiotrowska.databaseservice.entity.Book;
import com.apiotrowska.databaseservice.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JPAUnitTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    public void shouldCreateBook() {
        Book book = bookRepository.save(new Book("New book", "New Author", 2000, 100));
        assertThat(book).hasFieldOrPropertyWithValue("id", 10L);
        assertThat(book).hasFieldOrPropertyWithValue("title", "New book");
        assertThat(book).hasFieldOrPropertyWithValue("author", "New Author");
        assertThat(book).hasFieldOrPropertyWithValue("publicationYear", 2000);
        assertThat(book).hasFieldOrPropertyWithValue("pages", 100);
        Assertions.assertEquals( 10, bookRepository.findAll().size());
    }

    @Test
    public void shouldFindAllBooks() {
        List<Book> books = bookRepository.findAll();
        Assertions.assertEquals(9, books.size());
    }

    @Test
    public void shouldFindBookById() {

    }

}
