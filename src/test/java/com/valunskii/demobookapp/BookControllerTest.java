package com.valunskii.demobookapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valunskii.demobookapp.controller.BookController;
import com.valunskii.demobookapp.domain.Book;
import com.valunskii.demobookapp.dto.BookRequestDto;
import com.valunskii.demobookapp.exception.BookNotFoundException;
import com.valunskii.demobookapp.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Captor
    private ArgumentCaptor<BookRequestDto> argumentCaptor;

    @Test
    public void postingANewBookShouldCreateBookInTheDatabase() throws Exception {

        BookRequestDto bookRequest = new BookRequestDto();
        bookRequest.setAuthor("Duke");
        bookRequest.setTitle("Java 11");
        bookRequest.setIsbn("12345");

        when(bookService.createNewBook(argumentCaptor.capture())).thenReturn(1L);

        this.mockMvc
                .perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/v1/book/1"));

        assertThat(argumentCaptor.getValue().getAuthor(), is("Duke"));
        assertThat(argumentCaptor.getValue().getIsbn(), is("12345"));
        assertThat(argumentCaptor.getValue().getTitle(), is("Java 11"));
    }

    @Test
    public void allBooksEndpointShouldReturnTwoBooks() throws Exception {

        when(bookService.getAllBooks()).thenReturn(List.of(
                createBook(1L, "Java 11", "Duke", "12345"),
                createBook(2L, "Java EE 8", "Duke", "45")));

        this.mockMvc
                .perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Java 11")))
                .andExpect(jsonPath("$[0].author", is("Duke")))
                .andExpect(jsonPath("$[0].isbn", is("12345")))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getBookWithIdOneShouldReturnABook() throws Exception {

        when(bookService.getBookById(1L)).thenReturn(createBook(1L, "Java 11", "Duke", "12345"));

        this.mockMvc
                .perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title", is("Java 11")))
                .andExpect(jsonPath("$.author", is("Duke")))
                .andExpect(jsonPath("$.isbn", is("12345")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getBookWithUnknownIdShould404() throws Exception {

        when(bookService.getBookById(111L)).thenThrow(new BookNotFoundException("Book with id '111' not found"));

        this.mockMvc
                .perform(get("/api/v1/books/111"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBookWithKnownIdShouldUpdateTheBook() throws Exception {

        BookRequestDto bookRequest = new BookRequestDto();
        bookRequest.setAuthor("Duke");
        bookRequest.setTitle("Java 12");
        bookRequest.setIsbn("12345");

        when(bookService.updateBook(eq(1L), argumentCaptor.capture()))
                .thenReturn(createBook(1L, "Java 12", "Duke", "12345"));

        this.mockMvc
                .perform(put("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title", is("Java 12")))
                .andExpect(jsonPath("$.author", is("Duke")))
                .andExpect(jsonPath("$.isbn", is("12345")))
                .andExpect(jsonPath("$.id", is(1)));

        assertThat(argumentCaptor.getValue().getAuthor(), is("Duke"));
        assertThat(argumentCaptor.getValue().getIsbn(), is("12345"));
        assertThat(argumentCaptor.getValue().getTitle(), is("Java 12"));
    }

    @Test
    public void updateBookWithUnknownIdShouldReturn404() throws Exception {

        BookRequestDto bookRequest = new BookRequestDto();
        bookRequest.setAuthor("Duke");
        bookRequest.setTitle("Java 12");
        bookRequest.setIsbn("12345");

        when(bookService.updateBook(eq(111L), argumentCaptor.capture()))
                .thenThrow(new BookNotFoundException("Book with id '111' not found"));

        this.mockMvc
                .perform(put("/api/v1/books/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isNotFound());

        assertThat(argumentCaptor.getValue().getAuthor(), is("Duke"));
        assertThat(argumentCaptor.getValue().getIsbn(), is("12345"));
        assertThat(argumentCaptor.getValue().getTitle(), is("Java 12"));
    }

    private Book createBook(long id, String title, String author, String isbn) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }
}
