package br.com.QueryParamsEBuscaPaginada.repository;

import br.com.QueryParamsEBuscaPaginada.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
