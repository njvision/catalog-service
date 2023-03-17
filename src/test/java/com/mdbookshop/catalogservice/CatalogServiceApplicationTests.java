package com.mdbookshop.catalogservice;

import com.mdbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        var expectedBook = new Book("1231231231", "Moon Light", "Bill Small", 8.23);

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

    @Test
    void whenGetRequestWithIdThenBookReturned() {
        var bookIsbn = "1231231230";
        var bookToCreate = new Book(bookIsbn, "Moon Light", "Bill Small", 8.23);
        Book expectedBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

    @Test
    void whenPutRequestThenBookUpdated() {
        var bookIsbn = "1234567890";
        var createdBook = new Book(bookIsbn, "Moon Light", "Bill Small", 8.23);
        Book bookExpected = webTestClient
                .post()
                .uri("/books")
                .bodyValue(createdBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(book -> {
                    assertThat(book).isNotNull();
                })
                .returnResult().getResponseBody();
        var updatedBook = new Book(bookExpected.isbn(), bookExpected.title(), bookExpected.author(), 5.55);

        webTestClient
                .put()
                .uri("/books/" + bookIsbn)
                .bodyValue(updatedBook)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(book -> {
                    assertThat(book).isNotNull();
                    assertThat(book.price()).isEqualTo(updatedBook.price());
                });
    }

    @Test
    void whenDeleteRequestThenBookDeleted() {
        var bookIsbn = "1234567890";
        var createdBook = new Book(bookIsbn, "Moon Light", "Bill Small", 8.23);
        webTestClient
                .post()
                .uri("/books")
                .bodyValue(createdBook)
                .exchange()
                .expectStatus().isCreated();
        webTestClient
                .delete()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNoContent();
        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(expectedMessage -> {
                    assertThat(expectedMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found.");
                });
    }
}
