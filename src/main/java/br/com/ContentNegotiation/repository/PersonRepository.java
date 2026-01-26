package br.com.ContentNegotiation.repository;

import br.com.ContentNegotiation.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
