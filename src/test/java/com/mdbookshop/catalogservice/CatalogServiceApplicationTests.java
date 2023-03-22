package com.mdbookshop.catalogservice;

import com.mdbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        var expectedBook = Book.of("1231231231", "Moon Light", "Bill Small", 8.23, "Oraily");

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
        var bookToCreate = Book.of(bookIsbn, "Moon Light", "Bill Small", 8.23, "Oraily");
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
        var bookIsbn = "1234567892";
        var createdBook = Book.of(bookIsbn, "Moon Light", "Bill Small", 8.23, "Oraily");
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
        var updatedBook = new Book(bookExpected.id(), bookExpected.isbn(), bookExpected.title(), bookExpected.author(), 5.55,
                bookExpected.publisher(), bookExpected.createdDate(), bookExpected.lastModifiedDate(), bookExpected.version());

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
        var bookIsbn = "1234567893";
        var createdBook = Book.of(bookIsbn, "Moon Light", "Bill Small", 8.23, "Oraily");
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
