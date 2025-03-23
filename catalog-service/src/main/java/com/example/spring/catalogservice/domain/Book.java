package com.example.spring.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Builder
public record Book(

        @Id
        Long id,
        @NotBlank
        @Pattern(
                regexp = "^([0-9]{10}|[0-9]{13})",
                message = "The ISBN format must be valid."
        )
        String isbn,
        @NotBlank(
                message = "The title must be defined."
        )
        String title,
        @NotBlank(
                message = "The book author must be defined."
        )
        String author,
        @NotNull(
                message = "The book price must be defined."
        )
        @Positive(
                message = "The book price must be greater than zero."
        )
        Double price,
        @CreatedDate
        @Column("create_at")
        Instant createAt,
        @LastModifiedDate
        @Column("last_modified_at")
        Instant lastModifiedAt,
        @Version
        int version
) {
}
