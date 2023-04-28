package ru.clevertec.ecl.newsservice.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "%s was not found";
    private static final String DEFAULT_MESSAGE_WITH_ID = "%s with ID %s was not found";

    public <T> EntityNotFoundException(Class<T> entity) {
        super(String.format(DEFAULT_MESSAGE, entity.getSimpleName()));
    }

    public <T> EntityNotFoundException(Class<T> entity, Long id) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, entity.getSimpleName(), id));
    }
}
