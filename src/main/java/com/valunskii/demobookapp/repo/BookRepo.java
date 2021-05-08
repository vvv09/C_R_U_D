package com.valunskii.demobookapp.repo;

import com.valunskii.demobookapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
}
