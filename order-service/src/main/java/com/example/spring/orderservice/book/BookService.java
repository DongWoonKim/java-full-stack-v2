package com.example.spring.orderservice.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookClient bookClient;

    public Flux<Book> getBooks() {
        return bookClient.getBooks()
                .defaultIfEmpty(buildNotFoundBook());
    }

    public Mono<Book> enrollBook(Book book) {
        return bookClient.getBookByIsbn(book.isbn())
                .flatMap(existingBook -> Mono.just(Book.builder().message("Already Exist!").build()))
                .switchIfEmpty(bookClient.enrollBook(book));
    }

    public Mono<Book> updateBook(String isbn, Book book) {
        return bookClient.getBookByIsbn(isbn)
                .flatMap(existingBook -> bookClient.updateBook(isbn, book))
                .defaultIfEmpty(Book.builder().message("Not Exist!").build());
    }

    public Mono<Void> deleteBook(String isbn) {
        return bookClient.deleteBook(isbn);
    }

    private static Book buildNotFoundBook() {
        return Book.builder()
                .message("Book not found")
                .build();
    }
}
