package br.com.WorkingWithEmails.repository;

import br.com.WorkingWithEmails.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("""
        select p from Person p
        where p.firstName like lower(concat('%', :firstName, '%'))
        """)
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disablePerson(@Param("id") Long id);
}
