package ru.clevertec.ecl.authservice.exception;

public class TokenExpirationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Token expired";

    public TokenExpirationException() {
        super(DEFAULT_MESSAGE);
    }
}
