package br.com.ImportAndExportSpreadsheet.repository;

import br.com.ImportAndExportSpreadsheet.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
