package com.example.springintro.service;

import com.example.springintro.model.entity.Book;
import com.example.springintro.model.enums.EditionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findBooksTitlesByAgeRestriction(String input);

    List<String> getGoldenBooksTitlesWithLessThan5000Copies(EditionType editionType, int copies);

    List<String> getBooksTitlesByPrice(BigDecimal lowerPrice, BigDecimal greaterPrice);

    List<String> getBooksNotReleasedInGivenYear(int year);

    List<String> getBooksBeforeDate(String date);

    List<String> getBooksContainingGivenString(String input);

    List<String> getBooksByAuthorsLastNameStartingWithGivenString(String input);

    int getBookWithTitleLongerThanGivenNumber(int input);

    List<String> getTotalBookCopiesByAuthor();

    String getBookByTitle();

    int increaseCopiesOfBooksAfterGivenDate(String date, int copies);

    int removeBooksWithCopiesLowerThanAGivenNum();
}
