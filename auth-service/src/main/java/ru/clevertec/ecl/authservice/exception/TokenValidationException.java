package ru.clevertec.ecl.authservice.exception;

/**
 * Exception if the token is not valid
 *
 * @author Konstantin Voytko
 */
public class TokenValidationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Token is not valid";

    public TokenValidationException() {
        super(DEFAULT_MESSAGE);
    }
}
