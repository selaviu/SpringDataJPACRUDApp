package com.example.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    public Book findByTitleStartingWith(String start);
}
