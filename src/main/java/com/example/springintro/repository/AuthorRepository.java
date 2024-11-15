package com.example.springintro.repository;

import com.example.springintro.model.entity.Author;
import jakarta.persistence.NamedStoredProcedureQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY SIZE(a.books) DESC")
    List<Author> findAllByBooksSizeDESC();

    List<Author> findByFirstNameEndingWith(String input);

    @Procedure(procedureName = "GET_BOOKS_BY_AUTHOR", outputParameterName = "result")
    int getBooksCount(String firstName, String lastName);
}
