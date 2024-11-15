package com.example.springintro;

import com.example.springintro.model.entity.AuthorInfo;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookInfo;
import com.example.springintro.model.enums.AgeRestriction;
import com.example.springintro.model.enums.EditionType;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    Scanner scanner = new Scanner(System.in);

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        getBooksTitlesByAgeRestriction();
        getGoldenBooks();
        getBooksByPrice();
        getNotReleasedBooks();
        getBooksReleasedBeforeDate();
        getAuthorsSearch();
        getBooksSearch();
        getBookTitlesSearch();
        getCountBooks();
        getTotalBookCopies();
        getReducedBook();
        getIncreaseBookCopies();
        removeBooks();
        storedProcedure();
    }

    private void storedProcedure() {
        int authorsBooks = this.authorService.getAuthorsBooks("Amanda", "Rice");
        System.out.println(authorsBooks);
    }

    private void removeBooks() {
        int removedBooks = this.bookService.removeBooksWithCopiesLowerThanAGivenNum();
        System.out.println(removedBooks);
    }

    private void getIncreaseBookCopies() {
        String date = scanner.nextLine();
        int copies = Integer.parseInt(scanner.nextLine());

        int bookCopies = this.bookService.increaseCopiesOfBooksAfterGivenDate(date, copies);
        System.out.println(bookCopies);
    }

    private void getReducedBook() {
        String book = this.bookService.getBookByTitle();
        System.out.println(book);
    }

    private void getTotalBookCopies() {
        List<String> authors = this.bookService.getTotalBookCopiesByAuthor();

        System.out.println(authors);
    }

    private void getCountBooks() {
        int countBooks = this.bookService.getBookWithTitleLongerThanGivenNumber(40);
        System.out.println(countBooks);
    }

    private void getBookTitlesSearch() {
        List<String> books = this.bookService
                .getBooksByAuthorsLastNameStartingWithGivenString("Ric");

        System.out.println(books);
    }

    private void getBooksSearch() {
        List<String> books = this.bookService.getBooksContainingGivenString("sK");
        System.out.println(books);
    }

    private void getAuthorsSearch() {
        List<String> authors = this.authorService.getAuthorsEndingWithGivenString("dy");
        System.out.println(authors);
    }

    private void getBooksReleasedBeforeDate() {
        List<String> books = this.bookService.getBooksBeforeDate("12-04-1992");
        System.out.println(books);
    }

    private void getNotReleasedBooks() {
        List<String> books = this.bookService
                .getBooksNotReleasedInGivenYear(2000);
        System.out.println(books);
    }

    private void getBooksByPrice() {
        List<String> books = this.bookService
                .getBooksTitlesByPrice(BigDecimal.valueOf(5), BigDecimal.valueOf(40));
        System.out.println(books);
    }

    private void getGoldenBooks() {
        List<String> books = bookService
                .getGoldenBooksTitlesWithLessThan5000Copies(EditionType.GOLD, 5000);
        System.out.println(books);
    }

    private void getBooksTitlesByAgeRestriction() {
        List<String> books = this.bookService.findBooksTitlesByAgeRestriction("miNor");
        System.out.println(books);
    }

    private void seedData() throws IOException {
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();
    }
}
