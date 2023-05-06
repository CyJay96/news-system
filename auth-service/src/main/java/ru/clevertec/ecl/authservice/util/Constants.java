package ru.clevertec.ecl.authservice.util;

/**
 * Constants for use in a web application
 *
 * @author Konstantin Voytko
 */
public class Constants {

    public static final String EMAIL_REGEX = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";

    public static final String PHONE_REGEX = "^[\\+]?[0-9]{1,3}[(\\-\\.]?[0-9]{1,3}[)\\-\\.]?[0-9]{1,3}[-\\s\\.]?[0-9]{1,3}[-\\s\\.]?[0-9]{1,3}$";
}
