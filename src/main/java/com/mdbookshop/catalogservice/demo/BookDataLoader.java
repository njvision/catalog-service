package com.mdbookshop.catalogservice.demo;

import com.mdbookshop.catalogservice.domain.Book;
import com.mdbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("testdata")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        bookRepository.deleteAll();
        var book1 = Book.of("1234567891", "Ball fly", "Stiven Goor", 3.90, "Oraily");
        var book2 = Book.of("1234567892", "Joke", "Dana Shine", 6.52, "Oraily");
        bookRepository.saveAll(List.of(book1, book2));
    }
}