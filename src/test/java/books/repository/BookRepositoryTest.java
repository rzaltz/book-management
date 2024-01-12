package books.repository;

import books.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldSaveBook(){
        Book book = new Book();
        book.setTitle("Hello World");
        book.setAuthor("Rachel Zaltz");
        book.setIsbn("12345");
        book.setTags(List.of("Hello", "Education"));

        Book savedBook = bookRepository.save(book);

        assertEquals(savedBook.getTitle(), "Hello World");
        assertEquals(savedBook.getAuthor(), "Rachel Zaltz");
        assertEquals(savedBook.getIsbn(), "12345");
        assertEquals(savedBook.getTags(), List.of("Hello", "Education"));
    }

    @Test
    public void shouldGetSavedBooks(){
        Book book1 = new Book();
        book1.setTitle("Hello World");
        book1.setAuthor("Rachel Zaltz");
        book1.setIsbn("12345");
        book1.setTags(List.of("Hello", "Education"));
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Hello World part 2");
        book2.setAuthor("Rachel Zaltz");
        book2.setIsbn("789");
        book2.setTags(List.of("Hello", "Education"));
        bookRepository.save(book2);

        Iterable<Book> savedBooks = bookRepository.findAll();

        List<Book> bookList = iteratorToList(savedBooks);

        assertEquals(bookList.size(), 2);
    }

    @Test
    public void shouldDeleteBook(){
        Book book = new Book();
        book.setTitle("Hello World");
        book.setAuthor("Rachel Zaltz");
        book.setIsbn("12345");
        book.setTags(List.of("Hello", "Education"));
        bookRepository.save(book);

        bookRepository.deleteById("12345");

        Iterable<Book> savedBooks = bookRepository.findAll();

        List<Book> bookList = iteratorToList(savedBooks);

        assertEquals(bookList.size(), 0);
    }

    @Test
    public void shouldUpdateBook(){
        Book book = new Book();
        book.setTitle("Hello World");
        book.setAuthor("Rachel Zaltz");
        book.setIsbn("12345");
        book.setTags(List.of("Hello", "Education"));
        bookRepository.save(book);

        Book updatedBook = new Book();
        updatedBook.setTitle("Hello World");
        updatedBook.setAuthor("Rachel Sabrina Zaltz");
        updatedBook.setIsbn("12345");
        updatedBook.setTags(List.of("Hello", "Education"));

        Book savedUpdatedBook = bookRepository.save(updatedBook);

        assertEquals(savedUpdatedBook.getTitle(), "Hello World");
        assertEquals(savedUpdatedBook.getAuthor(), "Rachel Sabrina Zaltz");
        assertEquals(savedUpdatedBook.getIsbn(), "12345");
        assertEquals(savedUpdatedBook.getTags(), List.of("Hello", "Education"));

    }

    private List<Book> iteratorToList(Iterable<Book> iterable) {
        List<Book> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

}
