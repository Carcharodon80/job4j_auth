package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final BCryptPasswordEncoder encoder;

    public PersonController(PersonService personService, BCryptPasswordEncoder encoder) {
        this.personService = personService;
        this.encoder = encoder;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return personService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Человек с заданным id не найден"));
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Person person) {
        return Optional.ofNullable(personService.save(person))
                .<ResponseEntity<?>>map(savedPerson -> ResponseEntity.status(HttpStatus.CREATED).body(savedPerson))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка при сохранении"));
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody Person person) {
        return Optional.of(personService.update(person))
                .filter(updateSuccess -> updateSuccess)
                .<ResponseEntity<?>>map(s -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при редактировании"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        return Optional.of(personService.delete(person))
                .filter(deleteSuccess -> deleteSuccess)
                .<ResponseEntity<?>>map(s -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при удалении"));
    }
}

//todo доделать /login и проверить в curl

