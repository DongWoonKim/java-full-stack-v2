package com.example.spring.catalogservice.controller;

import com.example.spring.catalogservice.domain.Book;
import com.example.spring.catalogservice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookApiController {

    private final BookService bookService;

    @GetMapping
    public Iterable<Book> get() {
        return bookService.viewBookList();
    }

    @GetMapping("/{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        log.info("getByIsbn {}", isbn);
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    public Book post(@Valid @RequestBody Book book) {
        return bookService.addBookToCatalog(book);
    }

    @PutMapping("/{isbn}")
    public Book put(@PathVariable String isbn,@Valid @RequestBody Book book) {
        return bookService.editBookDetails(isbn, book);
    }

    @DeleteMapping("/{isbn}")
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

}
