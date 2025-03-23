package com.example.spring.orderservice.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class BookClient {
    private static final String BOOK_ROOT_API = "/books";
    private final WebClient webClient;

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient.get()
                .uri(BOOK_ROOT_API + "/" + isbn)
                .retrieve() // 요청을 보내고 응답을 받는다
                .bodyToMono(Book.class)
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }

    public Flux<Book> getBooks() {
        return webClient.get()
                .uri(BOOK_ROOT_API)
                .retrieve()
                .bodyToFlux(Book.class)
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Flux.empty())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, exception -> Flux.empty());
    }

    public Mono<Book> enrollBook(Book book) {
        return webClient.post()
                .uri(BOOK_ROOT_API)
                .bodyValue(book)
                .retrieve()
                .bodyToMono(Book.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());

    }

    public Mono<Book> updateBook(String isbn, Book book) {
        return webClient.put()
                .uri(BOOK_ROOT_API + "/" + isbn)
                .bodyValue(book)
                .retrieve()
                .bodyToMono(Book.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }

    public Mono<Void> deleteBook(String isbn) {
        return webClient.delete()
                .uri(BOOK_ROOT_API + "/" +isbn)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }
}
