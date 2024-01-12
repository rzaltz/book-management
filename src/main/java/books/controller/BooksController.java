package books.controller;


import books.model.Book;
import books.model.BookSearchCriteria;
import books.service.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;

    @PostMapping
    @Operation(summary = "Create a book")
    public Book create(@RequestBody Book book) {
        return booksService.createBook(book);
    }

    @GetMapping
    @Operation(summary = "Find all books")
    public Iterable<Book> findAllBooks() {
        return booksService.findAllBooks();
    }


    @GetMapping(value = "/search")
    @Operation(summary = "Search books")
    public List<Book> searchByCriteria(@ModelAttribute BookSearchCriteria bookSearchCriteria) {
        return booksService.findByCriteria(bookSearchCriteria);
    }

    @PutMapping
    @Operation(summary = "Update a book")
    public Book updateBook(@RequestBody Book book) {
        return booksService.updateBook(book);
    }


    @DeleteMapping("/{isbn}")
    @Operation(summary = "Delete a book")
    public void delete(@PathVariable String isbn) {
        booksService.deleteBook(isbn);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import a CSV file of books",
            description = "To upload a file it should follow this criteria: " +
                    "1. Be a CSV file. " +
                    "2. The table should have a header with the following: ISBN, Title, Author, Tags " +
                    "3. The tags should be comma separated")
    public Iterable<Book> importBooks(@RequestParam("file") MultipartFile file) {
        return booksService.uploadBooks(file);
    }
}
