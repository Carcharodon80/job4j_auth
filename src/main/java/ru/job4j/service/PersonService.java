package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        try {
            return personRepository.save(person);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean update(Person person) {
        if (!personRepository.existsById(person.getId())) {
            return false;
        }
        try {
            personRepository.save(person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Person person) {
        try {
            personRepository.delete(person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
