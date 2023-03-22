package com.mdbookshop.catalogservice.domain;

import com.mdbookshop.catalogservice.config.DataConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    public void findAllBooks() {
        var bookFirst = Book.of("1234567893", "Title", "Author", 5.50, "Oraily");
        var bookSecond = Book.of("1234567894", "Title", "Author", 5.50, "Oraily");

        jdbcAggregateTemplate.insert(bookFirst);
        jdbcAggregateTemplate.insert(bookSecond);

        Iterable<Book> actualBooks = bookRepository.findAll();

        assertThat(StreamSupport.stream(actualBooks.spliterator(), true).filter(book -> book.isbn().equals(bookFirst.isbn()) || book.isbn().equals(bookSecond.isbn())).collect(Collectors.toList())).hasSize(2);
    }

    @Test
    public void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234567895";
        var book = Book.of(bookIsbn, "Title", "Author", 5.50, "Oraily");
        jdbcAggregateTemplate.insert(book);

        Optional<Book> bookByIsbn = bookRepository.findByIsbn(bookIsbn);

        assertThat(bookByIsbn).isPresent();
        assertThat(bookByIsbn.get().isbn()).isEqualTo(book.isbn());
    }

    @Test
    public void findBookByIsbnWhenNotExisting() {
        Optional<Book> actualBook = bookRepository.findByIsbn("1234567896");
        assertThat(actualBook).isEmpty();
    }

    @Test
    public void existsByIsbnWhenExisting() {
        var bookIsbn = "1234567897";
        var book = Book.of(bookIsbn, "Title", "Author", 5.50, "Oraily");

        boolean checkExistence = bookRepository.existsByIsbn(bookIsbn);

        assertThat(checkExistence).isTrue();
    }

    @Test
    public void existsByIsbnWhenNotExisting() {
        var bookIsbn = "1234567898";
        boolean checkExistence = bookRepository.existsByIsbn(bookIsbn);

        assertThat(checkExistence).isFalse();
    }

    @Test
    public void deleteByIsbn() {
        var bookIsbn = "1234567899";
        var book = Book.of(bookIsbn, "Title", "Author", 5.50, "Oraily");

        Book persistedBook = jdbcAggregateTemplate.insert(book);

        bookRepository.deleteByIsbn(bookIsbn);

        assertThat(jdbcAggregateTemplate.findById(persistedBook.id(), Book.class)).isNull();

    }
}
