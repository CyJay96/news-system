package ru.clevertec.ecl.authservice.exception;

public class UserExistenceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "User with such name %s or email %s already exists";

    public UserExistenceException(String username, String email) {
        super(String.format(DEFAULT_MESSAGE, username, email));
    }
}
