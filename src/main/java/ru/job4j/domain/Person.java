package ru.job4j.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "person")
@Setter
@Getter
@EqualsAndHashCode
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id не может быть null", groups = {
            Operation.OnUpdate.class, Operation.OnPatch.class})
    private int id;

    @Column(unique = true)
    @NotNull(message = "Login не может быть null")
    @Size(min = 3, max = 50, message = "Login может быть от 3 до 50 символов")
    private String login;

    @NotNull(message = "Password не может быть null")
    @Size(min = 3, message = "Password должен содержать не менее 3 символов")
    private String password;

    @AssertTrue(message = "Login не может быть 'Anonimus'")
    public boolean isLoginNotAnonimus() {
        return !login.equalsIgnoreCase("Anonimus");
    }
}
