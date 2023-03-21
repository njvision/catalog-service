package com.mdbookshop.catalogservice.demo;

import com.mdbookshop.catalogservice.domain.Book;
import com.mdbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        var book1 = new Book("1234567891", "Ball fly", "Stiven Goor", 3.90);
        var book2 = new Book("1234567892", "Joke", "Dana Shine", 6.52);
        bookRepository.save(book1);
        bookRepository.save(book2);
    }

}