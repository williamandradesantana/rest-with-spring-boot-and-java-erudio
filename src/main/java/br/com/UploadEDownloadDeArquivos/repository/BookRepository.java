package br.com.UploadEDownloadDeArquivos.repository;

import br.com.UploadEDownloadDeArquivos.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
