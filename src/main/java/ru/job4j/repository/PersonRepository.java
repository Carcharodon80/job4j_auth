package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    @NonNull
    @Query("from person")
    List<Person> findAll();

    Optional<Person> findByLogin(String username);
}
