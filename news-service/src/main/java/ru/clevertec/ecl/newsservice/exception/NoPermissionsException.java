package ru.clevertec.ecl.newsservice.exception;

/**
 * Exception if there are no permissions to perform the operation
 *
 * @author Konstantin Voytko
 */
public class NoPermissionsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "There are no permissions";

    public NoPermissionsException() {
        super(DEFAULT_MESSAGE);
    }
}
