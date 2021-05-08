package com.valunskii.demobookapp;

import com.github.javafaker.Faker;
import com.valunskii.demobookapp.domain.Book;
import com.valunskii.demobookapp.repo.BookRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class BookInitializer implements CommandLineRunner {

    private final BookRepo bookRepo;

    @Override
    public void run(String... args) {

        log.info("Starting to create sample data ...");

        Faker faker = new Faker();

        for (int i=0; i < 100; i++) {
            Book book = new Book();
            book.setAuthor(faker.book().author());
            book.setTitle(faker.book().title());
            book.setIsbn(UUID.randomUUID().toString());

            bookRepo.save(book);

        }

        log.info("... finished with data initialization");
    }
}
