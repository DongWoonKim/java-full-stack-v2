package com.example.spring.catalogservice.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
//    Iterable<Book> findAll();
    Optional<Book> findByIsbn(String isbn);
//    Book save(Book book);
//    void deleteByIsbn(String isbn);
    // 데이터베이스 상태를 수정할 연산임을 나타낸다.
    @Modifying
    @Transactional
    @Query("DELETE FROM book WHERE isbn=:isbn")
    void deleteByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}
