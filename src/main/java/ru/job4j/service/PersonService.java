package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.dto.PasswordIdDto;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder encoder;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByName(String name) {
        return personRepository.findByLogin(name);
    }

    public Optional<Person> save(Person person) {
        try {
            return Optional.of(personRepository.save(person));
        } catch (Exception e) {
            return Optional.empty();
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

    public Optional<Boolean> patch(PasswordIdDto passwordIdDto) {
        return findById(passwordIdDto.getId())
                .map(person -> {
                            person.setPassword(encoder.encode(passwordIdDto.getPassword()));
                            return update(person);
                        }
                );
    }

    public Optional<Boolean> delete(int id) {
        try {
            personRepository.deleteById(id);
            return Optional.of(true);
        } catch (Exception e) {
            return Optional.of(false);
        }
    }
}
