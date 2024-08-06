package ru.job4j.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PersonDetailsServiceImpl implements UserDetailsService {
    private final PersonService personService;

    public PersonDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = personService.findByName(userName);
        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException(userName);
        }
        return new User(optionalPerson.get().getLogin(), optionalPerson.get().getPassword(), new ArrayList<>());
    }
}
