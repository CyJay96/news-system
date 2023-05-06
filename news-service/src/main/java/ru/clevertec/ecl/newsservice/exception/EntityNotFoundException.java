package ru.clevertec.ecl.newsservice.exception;

/**
 * Exception if the entity was not found
 *
 * @author Konstantin Voytko
 */
public class EntityNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "%s was not found";
    private static final String DEFAULT_MESSAGE_WITH_ID = "%s with ID %s was not found";
    private static final String DEFAULT_MESSAGE_WITH_DATA = "%s was not found";

    public EntityNotFoundException(String message) {
        super(String.format(DEFAULT_MESSAGE_WITH_DATA, message));
    }

    public <T> EntityNotFoundException(Class<T> entity) {
        super(String.format(DEFAULT_MESSAGE, entity.getSimpleName()));
    }

    public <T> EntityNotFoundException(Class<T> entity, Long id) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, entity.getSimpleName(), id));
    }
}
