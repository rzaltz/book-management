package books.service;

import books.exception.InvalidBookException;
import books.model.Book;
import books.model.BookSearchCriteria;
import books.repository.BookRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BooksService {

    private final BookRepository bookRepository;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book updateBook(Book book) {
        findByIsbnOrThrow(book.getIsbn(), String.format("Invalid book: %s", book.getIsbn()));
        return bookRepository.save(book);
    }

    public void deleteBook(String isbn) {
        findByIsbnOrThrow(isbn, String.format("Invalid book: %s", isbn));
        bookRepository.deleteById(isbn);
    }

    public Iterable<Book> uploadBooks(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            reader.skip(1);
            List<String[]> rows = reader.readAll();
            List<Book> books = parseCsv(rows);
            return bookRepository.saveAll(books);
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Book> parseCsv(List<String[]> rows) {
        return rows.stream()
                .map(this::convertToBook)
                .collect(Collectors.toList());
    }

    private Book convertToBook(String[] row) {
        return Book.builder()
                .isbn(row[0])
                .title(row[1])
                .author(row[2])
                .tags(List.of(row[3].split(",")))
                .build();

    }

    public List<Book> findByCriteria(BookSearchCriteria searchCriteria) {
        List<Book> books = iterableToList(bookRepository.findAll());
        return books.stream()
                .filter(bookCriteria(searchCriteria))
                .collect(Collectors.toList());
    }

    private Predicate<Book> bookCriteria(BookSearchCriteria searchCriteria) {
        return book ->
                (searchCriteria.getTitles() == null || searchCriteria.getTitles().isEmpty() || searchCriteria.getTitles().contains(book.getTitle())) &&
                        (searchCriteria.getAuthors() == null || searchCriteria.getAuthors().isEmpty() || searchCriteria.getAuthors().contains(book.getAuthor())) &&
                        (searchCriteria.getTags() == null || searchCriteria.getTags().isEmpty() || book.getTags().stream().anyMatch(searchCriteria.getTags()::contains)) &&
                        (searchCriteria.getIsbns() == null || searchCriteria.getIsbns().isEmpty() || searchCriteria.getIsbns().contains(book.getIsbn()));
    }


    private Book findByIsbnOrThrow(String isbn, String customErrorMessage) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        return optionalBook.orElseThrow(() -> new InvalidBookException(customErrorMessage));
    }

    private List<Book> iterableToList(Iterable<Book> iterable) {
        List<Book> list = new ArrayList<>();

        iterable.forEach(list::add);

        return list;
    }

}
