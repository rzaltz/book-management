package books.service;


import books.exception.InvalidBookException;
import books.model.Book;
import books.model.BookSearchCriteria;
import books.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BooksService booksService;

    @Test
    public void shouldThrowException_IfBookToDeleteDoesNotExist() {
        Optional<Book> optionalBook = Optional.empty();
        when(bookRepository.findById("1234")).thenReturn(optionalBook);

        assertThrows(InvalidBookException.class, () -> {
            booksService.deleteBook("1234");
        });
    }

    @Test
    public void shouldThrowException_IfBookToUpdateDoesNotExist() {
        Optional<Book> optionalBook = Optional.empty();
        when(bookRepository.findById("1234")).thenReturn(optionalBook);

        Book book = new Book();
        book.setTitle("Hello World");
        book.setAuthor("Rachel Zaltz");
        book.setIsbn("1234");
        book.setTags(List.of("Hello", "Education"));

        assertThrows(InvalidBookException.class, () -> {
            booksService.updateBook(book);
        });
    }

    @ParameterizedTest
    @MethodSource("argumentsForBookSearchCriteria")
    public void shouldFilterByBookSearchCriteria(BookSearchCriteria bookSearchCriteria, Integer size) {
        when(bookRepository.findAll()).thenReturn(mockBooks());
        List<Book> book = booksService.findByCriteria(bookSearchCriteria);
        assertEquals(book.size(), size);

    }

    private static Stream<Arguments> argumentsForBookSearchCriteria(){
        return Stream.of(
                Arguments.of(BookSearchCriteria.builder()
                        .isbns(List.of("1234"))
                        .build(), 1),
                Arguments.of(BookSearchCriteria.builder()
                        .titles(List.of("Hello World", "Canada"))
                        .build(), 2),
                Arguments.of(BookSearchCriteria.builder()
                        .titles(List.of("Hello World", "Canada"))
                        .authors(List.of("Zaltz"))
                        .build(), 1),
                Arguments.of(BookSearchCriteria.builder()
                        .build(), 3),
                Arguments.of(BookSearchCriteria.builder()
                        .tags(List.of("Hello", "Education", "Java"))
                        .build(), 2),
                Arguments.of(BookSearchCriteria.builder()
                        .tags(List.of("Hello", "Education", "Java", "Testing"))
                        .build(), 3),
                Arguments.of(BookSearchCriteria.builder()
                        .titles(List.of("Hello World", "Canada", "Books"))
                        .authors(List.of("Rachel Zaltz", "Zaltz", "Rachel"))
                        .isbns(List.of("1234", "789", "111"))
                        .tags(List.of("Hello", "Education", "Java", "Testing"))
                        .build(), 3)
        );
    }

    private Iterable<Book> mockBooks (){
        Book book = new Book();
        book.setTitle("Hello World");
        book.setAuthor("Rachel Zaltz");
        book.setIsbn("1234");
        book.setTags(List.of("Hello", "Education"));

        Book book1 = new Book();
        book1.setTitle("Canada");
        book1.setAuthor("Zaltz");
        book1.setIsbn("789");
        book1.setTags(List.of("Java"));

        Book book2 = new Book();
        book2.setTitle("Books");
        book2.setAuthor("Rachel");
        book2.setIsbn("111");
        book2.setTags(List.of("Testing"));

        return listToIterable(List.of(book, book1, book2));
    }

    private static Iterable<Book> listToIterable(List<Book> list) {
        return list::iterator;
    }
}
