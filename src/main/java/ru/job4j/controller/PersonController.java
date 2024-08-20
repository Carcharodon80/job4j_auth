package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.exception.JohnAndJaneDoeException;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    public PersonController(PersonService personService, BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.personService = personService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(
                personService.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id не может быть меньше 0");
        }
        Person user = personService.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Человек с заданным id не найден"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Person person) {
        if (person.getLogin().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Логин или пароль не могут быть пустыми.");
        }
        if (person.getLogin().equals("John Doe") || person.getLogin().equals("Jane Doe")) {
            throw new JohnAndJaneDoeException("John and Jane Doe не разрешается.");
        }
        ResponseEntity<?> entity;
        person.setPassword(encoder.encode(person.getPassword()));
        if (personService.save(person).isPresent()) {
            entity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(person);
        } else {
            entity = ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка при сохранении.");
        }
        return entity;
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Логин или пароль не могут быть пустыми.");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return Optional.of(personService.update(person))
                .filter(updateSuccess -> updateSuccess)
                .<ResponseEntity<?>>map(s -> ResponseEntity
                        .status(HttpStatus.OK)
                        .header("updateCustomHeader", "true")
                        .body("Обновление успешно."))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при редактировании"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id не может быть меньше 0");
        }
        byte[] image = new byte[0];
        try {
            image = Files.readAllBytes(Paths.get("./src/main/resources/ok.png"));
        } catch (IOException e) {
            LOGGER.error("Файл с изображением не найден.");
        }
        byte[] finalImage = image;
        return personService.delete(id)
                .filter(deleteSuccess -> deleteSuccess)
                .<ResponseEntity<?>>map(s -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.IMAGE_PNG)
                        .contentLength(finalImage.length)
                        .body(finalImage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка при удалении"));
    }

    @ExceptionHandler(value = JohnAndJaneDoeException.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }
}