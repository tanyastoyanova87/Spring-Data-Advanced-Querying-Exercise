package com.example.springintro.repository;

import com.example.springintro.model.entity.AuthorInfo;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookInfo;
import com.example.springintro.model.enums.AgeRestriction;
import com.example.springintro.model.enums.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findByEditionTypeAndCopiesLessThan(EditionType editionType, int copies);

    List<Book> findByPriceLessThanOrPriceGreaterThan(BigDecimal lowerPrice, BigDecimal greaterPrice);

    List<Book> findByReleaseDateBeforeOrReleaseDateAfter(LocalDate start, LocalDate end);

    List<Book> findByTitleContainingIgnoreCase(String input);

    List<Book> findByAuthorLastNameStartingWith(String input);

    @Query("SELECT COUNT(b) FROM Book AS b " +
            "WHERE LENGTH(b.title) > :input " )
    int findByTitleSizeLongerThan(int input);

    @Query("SELECT a.firstName AS firstName, a.lastName AS lastName, SUM(b.copies) AS total " +
            "FROM Book AS b " +
            "JOIN b.author AS a " +
            "GROUP BY b.author.id " +
            "ORDER BY total DESC")
    List<AuthorInfo> findByAuthorCopies();

    BookInfo getBookByTitle(String title);

    @Modifying
    @Transactional
    @Query("UPDATE Book AS b " +
            "SET b.copies = b.copies + :number " +
            "WHERE b.releaseDate > :date")
    int getBookByCopies(LocalDate date, int number);

    @Modifying
    @Transactional
    @Query("DELETE FROM Book AS b " +
            "WHERE b.copies < :number")
    int getRemovedBooks(int number);
}
