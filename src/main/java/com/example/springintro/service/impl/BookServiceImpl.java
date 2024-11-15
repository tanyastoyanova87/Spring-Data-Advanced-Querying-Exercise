package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.model.enums.AgeRestriction;
import com.example.springintro.model.enums.EditionType;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository
                .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year) {
        return bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
        return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(book -> String.format("%s %s %d",
                        book.getTitle(),
                        book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksTitlesByAgeRestriction(String input) {
        List<Book> books = this.bookRepository
                .findByAgeRestriction(AgeRestriction.valueOf(input.toUpperCase()));

        return getTitlesOfBooks(books);
    }

    @Override
    public List<String> getGoldenBooksTitlesWithLessThan5000Copies(EditionType editionType, int copies) {
        List<Book> books = this.bookRepository
                .findByEditionTypeAndCopiesLessThan(editionType, copies);

        return getTitlesOfBooks(books);
    }

    private static List<String> getTitlesOfBooks(List<Book> books) {
        return books.stream()
                .map(Book::getTitle)
                .toList();
    }

    @Override
    public List<String> getBooksTitlesByPrice(BigDecimal lowerPrice, BigDecimal greaterPrice) {
        List<Book> books = this.bookRepository
                .findByPriceLessThanOrPriceGreaterThan(lowerPrice, greaterPrice);

        return books.stream()
                .map(book -> String.format("%s - $%s", book.getTitle(), book.getPrice()))
                .toList();
    }

    @Override
    public List<String> getBooksNotReleasedInGivenYear(int year) {
        LocalDate startYear = LocalDate.of(year, 1, 1);
        LocalDate endYear = LocalDate.of(year, 12, 31);
        List<Book> books = this.bookRepository.findByReleaseDateBeforeOrReleaseDateAfter(startYear, endYear);

        return getTitlesOfBooks(books);
    }

    @Override
    public List<String> getBooksBeforeDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Book> books = this.bookRepository
                .findAllByReleaseDateBefore(localDate);

        return books.stream()
                .map(book -> String.format("%s %s %s",
                        book.getTitle(), book.getEditionType().name(), book.getPrice()))
                .toList();
    }

    @Override
    public List<String> getBooksContainingGivenString(String input) {
        List<Book> books = this.bookRepository.findByTitleContainingIgnoreCase(input);

        return getTitlesOfBooks(books);
    }

    @Override
    public List<String> getBooksByAuthorsLastNameStartingWithGivenString(String input) {
        List<Book> books = this.bookRepository.findByAuthorLastNameStartingWith(input);

        return books.stream()
                .map(b -> String.format("%s (%s %s)",
                        b.getTitle(), b.getAuthor().getFirstName(), b.getAuthor().getLastName()))
                .toList();
    }

    @Override
    public int getBookWithTitleLongerThanGivenNumber(int input) {
        return this.bookRepository.findByTitleSizeLongerThan(input);
    }

    @Override
        public List<String> getTotalBookCopiesByAuthor() {
        List<AuthorInfo> authors = this.bookRepository.findByAuthorCopies();

        return authors.stream()
                .map(a -> String.format("%s %s - %d",
                        a.getFirstName(), a.getLastName(), a.getTotal()))
                .toList();
    }

    @Override
    public String getBookByTitle() {
        BookInfo book = this.bookRepository.getBookByTitle("Things Fall Apart");

        return String.format("%s %s %s %s", book.getTitle(),
                book.getEditionType().name(), book.getAgeRestriction().name(), book.getPrice());
    }

    @Override
    public int increaseCopiesOfBooksAfterGivenDate(String date, int copies) {
        LocalDate parsedDated = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));

        return this.bookRepository.getBookByCopies(parsedDated, copies) * copies;
    }

    @Override
    public int removeBooksWithCopiesLowerThanAGivenNum() {
        return this.bookRepository.getRemovedBooks(1000);
    }

    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);

    }
}
