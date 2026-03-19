package br.com.WorkingWithJasperReports.repository;

import br.com.WorkingWithJasperReports.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
