package ru.job4j.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.job4j.handlers.Operation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
public class PasswordIdDto {
    @NotNull(message = "Id не может быть null", groups = Operation.OnPatch.class)
    private int id;
    @NotNull(message = "Password не может быть null")
    @NotBlank(message = "Password не может быть пустым")
    @Size(min = 3, message = "Password должен содержать не менее 3 символов")
    private String password;
}
