package br.com.WorkingWithEmails.repository;

import br.com.WorkingWithEmails.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
