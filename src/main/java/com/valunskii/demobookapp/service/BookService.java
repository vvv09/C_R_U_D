package com.valunskii.demobookapp.service;

import com.valunskii.demobookapp.domain.Book;
import com.valunskii.demobookapp.dto.BookRequestDto;
import com.valunskii.demobookapp.exception.BookNotFoundException;
import com.valunskii.demobookapp.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo repo;

    public Long createNewBook(BookRequestDto bookRequestDto) {

        Book book = new Book();
        book.setAuthor(bookRequestDto.getAuthor());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setTitle(bookRequestDto.getTitle());

        book = repo.save(book);

        return book.getId();
    }

    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public Book getBookById(Long id) {
        Optional<Book> requestedBook = repo.findById(id);
        if (requestedBook.isEmpty()) {
            throw new BookNotFoundException(String.format("Book with id '%s' not found", id));
        }
        return requestedBook.get();
    }

    @Transactional
    public Book updateBook(Long id, BookRequestDto bookToUpdateRequest) {
        Book bookToUpdate = getBookById(id);

        bookToUpdate.setAuthor(bookToUpdateRequest.getAuthor());
        bookToUpdate.setIsbn(bookToUpdateRequest.getIsbn());
        bookToUpdate.setTitle(bookToUpdateRequest.getTitle());

        return bookToUpdate;
    }

    public void deleteBookById(Long id) {
        Book bookToDelete = getBookById(id);
        repo.delete(bookToDelete);
    }
}
