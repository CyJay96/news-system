package ru.clevertec.ecl.authservice.exception;

/**
 * Exception if the token was expired
 *
 * @author Konstantin Voytko
 */
public class TokenExpirationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Token was expired";

    public TokenExpirationException() {
        super(DEFAULT_MESSAGE);
    }
}
