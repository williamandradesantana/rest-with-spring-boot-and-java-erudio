package br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.repository;

import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
