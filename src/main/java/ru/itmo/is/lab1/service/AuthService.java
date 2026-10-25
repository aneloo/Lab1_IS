package ru.itmo.is.lab1.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.dto.UserDto;
import ru.itmo.is.lab1.exception.AuthenticationException;
import ru.itmo.is.lab1.exception.TranslatePersistenceErrors;
import ru.itmo.is.lab1.exception.ValidationException;
import ru.itmo.is.lab1.mapper.UserMapper;
import ru.itmo.is.lab1.model.AppUser;
import ru.itmo.is.lab1.repository.UserRepository;
import ru.itmo.is.lab1.security.PasswordHasher;

@Stateless
@TranslatePersistenceErrors
public class AuthService {

    private static final String INVALID_CREDENTIALS = "Неверный логин или пароль";

    @Inject
    private UserRepository users;

    @Inject
    private UserMapper mapper;

    public UserDto register(String username, String password, String passwordRepeat) {
        String name = username == null ? "" : username.trim();
        if (name.length() < 3 || name.length() > 64) {
            throw new ValidationException("Логин должен содержать от 3 до 64 символов");
        }
        if (!name.matches("[A-Za-z0-9_.-]+")) {
            throw new ValidationException("Логин может содержать только латинские буквы, цифры и символы _ . -");
        }
        if (password == null || password.length() < 4) {
            throw new ValidationException("Пароль должен быть не короче 4 символов");
        }
        if (!password.equals(passwordRepeat)) {
            throw new ValidationException("Пароли не совпадают");
        }
        if (users.findByUsername(name).isPresent()) {
            throw new ValidationException("Пользователь «" + name + "» уже существует");
        }
        AppUser user = new AppUser(name, PasswordHasher.hash(password.toCharArray()));
        users.persist(user);
        return mapper.toDto(user);
    }

    public UserDto authenticate(String username, String password) {
        if (username == null || password == null) {
            throw new AuthenticationException(INVALID_CREDENTIALS);
        }
        return users.findByUsername(username.trim())
                .filter(u -> PasswordHasher.verify(password.toCharArray(), u.getPasswordHash()))
                .map(mapper::toDto)
                .orElseThrow(() -> new AuthenticationException(INVALID_CREDENTIALS));
    }
}
