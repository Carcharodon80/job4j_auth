package ru.job4j.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PasswordIdDto {
    private int id;
    private String password;
}
