package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return  personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        ResponseEntity<?> result;
        Optional<Person> person = personService.findById(id);
        if (person.isPresent()) {
            result = ResponseEntity.ok(person.get());
        } else {
            result = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Человек с заданным id не найден.");
        }
        return result;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Person person) {
        ResponseEntity<?> result;
        Person savedPerson = personService.save(person);
        if (savedPerson != null) {
            result = ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
        } else {
            result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при сохранении.");
        }
        return result;
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody Person person) {
        ResponseEntity<?> result;
        if (personService.update(person)) {
            result = ResponseEntity.ok().build();
        } else {
            result = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при редактировании.");
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        ResponseEntity<?> result;
        if (personService.delete(person)) {
            result = ResponseEntity.ok().build();
        } else {
            result = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при удалении.");
        }
        return result;
    }
}

