package br.com.Swagger.services;

import br.com.Swagger.exception.RequiredObjectIsNullException;
import br.com.Swagger.data.dto.BookDTO;
import br.com.Swagger.model.Book;
import br.com.Swagger.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {
    Book book;
    BookDTO bookDTO;

    @InjectMocks
    BookServices services;
    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Author Test", "20/01/2026", 20.0, "Title Test");
        bookDTO = new BookDTO(1L, "Author Test", "20/01/2026", 20.0, "Title Test");
    }

    @Test
    void findAll() {
        Book bookOne = new Book(1L, "Author Test 1", "20/01/2026", 20.0, "Title Test 1");
        Book bookTwo = new Book(2L, "Author Test 2", "21/01/2026", 25.0, "Title Test 2");

        List<Book> books = List.of(bookOne, bookTwo);

        when(repository.findAll()).thenReturn(books);

        var result = services.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        var firstBook = result.get(0);

        assertNotNull(firstBook.getId());
        assertNotNull(firstBook.getLinks());

        assertTrue(firstBook.getLinks().hasLink("self"));
        assertTrue(firstBook.getLinks().hasLink("findAll"));
        assertTrue(firstBook.getLinks().hasLink("create"));
        assertTrue(firstBook.getLinks().hasLink("update"));
        assertTrue(firstBook.getLinks().hasLink("delete"));

        assertEquals("Author Test 1", firstBook.getAuthor());
        assertEquals("20/01/2026", firstBook.getLaunchDate());
        assertEquals(20.0, firstBook.getPrice());
        assertEquals("Title Test 1", firstBook.getTitle());

        var secondBook = result.get(1);

        assertNotNull(secondBook.getId());
        assertNotNull(secondBook.getLinks());

        assertTrue(secondBook.getLinks().hasLink("self"));
        assertTrue(secondBook.getLinks().hasLink("findAll"));
        assertTrue(secondBook.getLinks().hasLink("create"));
        assertTrue(secondBook.getLinks().hasLink("update"));
        assertTrue(secondBook.getLinks().hasLink("delete"));

        assertEquals("Author Test 2", secondBook.getAuthor());
        assertEquals("21/01/2026", secondBook.getLaunchDate());
        assertEquals(25.0, secondBook.getPrice());
        assertEquals("Title Test 2", secondBook.getTitle());
    }

    @Test
    void findById() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = services.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().hasLink("self"));
        assertTrue(result.getLinks().hasLink("findAll"));
        assertTrue(result.getLinks().hasLink("create"));
        assertTrue(result.getLinks().hasLink("update"));
        assertTrue(result.getLinks().hasLink("delete"));

        assertEquals("Author Test", result.getAuthor());
        assertEquals("20/01/2026", result.getLaunchDate());
        assertEquals(20.0, result.getPrice());
        assertEquals("Title Test", result.getTitle());
    }

    @Test
    void create() {
        Book persisted = book;
        persisted.setId(1L);

        when(repository.save(any(Book.class))).thenReturn(persisted);

        var result = services.create(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().hasLink("self"));
        assertTrue(result.getLinks().hasLink("findAll"));
        assertTrue(result.getLinks().hasLink("create"));
        assertTrue(result.getLinks().hasLink("update"));
        assertTrue(result.getLinks().hasLink("delete"));

        assertEquals("Author Test", result.getAuthor());
        assertEquals("20/01/2026", result.getLaunchDate());
        assertEquals(20.0, result.getPrice());
        assertEquals("Title Test", result.getTitle());
    }

    @Test
    void testCreateNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
           services.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book persisted = book;
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        var result = services.update(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().hasLink("self"));
        assertTrue(result.getLinks().hasLink("findAll"));
        assertTrue(result.getLinks().hasLink("create"));
        assertTrue(result.getLinks().hasLink("update"));
        assertTrue(result.getLinks().hasLink("delete"));

        assertEquals("Author Test", result.getAuthor());
        assertEquals("20/01/2026", result.getLaunchDate());
        assertEquals(20.0, result.getPrice());
        assertEquals("Title Test", result.getTitle());
    }

    @Test
    void testUpdateNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            services.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        services.delete(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }
}