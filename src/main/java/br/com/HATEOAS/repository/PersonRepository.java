package br.com.HATEOAS.repository;

import br.com.HATEOAS.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
