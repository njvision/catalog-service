package com.mdbookshop.catalogservice.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public record Book(
        @NotBlank(message = "The book ISBN must be defined.")
        @Pattern(
                message = "The ISBN format must be valid.",
                regexp = "^([0-9]{10}|[0-9]{13})$"
        )
        String isbn,

        @NotBlank(message = "The book title must be defined.")
        String title,

        @NotBlank(message = "The book author must be defined.")
        String author,

        @Positive(message = "The book price must be greater than zero.")
        @NotNull(message = "The book price must be defined.")
        Double price
) {
}
