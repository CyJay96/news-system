package ru.clevertec.ecl.newsservice.util;

import java.time.OffsetDateTime;

public class TestConstants {

    public static final Integer TEST_PAGE = 0;
    public static final Integer TEST_PAGE_SIZE = 100;

    public static final Long TEST_ID = 1L;
    public static final Long TEST_NUMBER = 1L;
    public static final String TEST_STRING = "test_string";
    public static final OffsetDateTime TEST_DATE = OffsetDateTime.now();

    public static final String RESPONSE = "{\n" +
            "  \"timestamp\": \"2023-05-02T23:28:48.4426202+03:00\",\n" +
            "  \"status\": 200,\n" +
            "  \"message\": \"User with username Mike was found\",\n" +
            "  \"path\": \"/api/v0/users/Mike\",\n" +
            "  \"data\": {\n" +
            "    \"id\": 1,\n" +
            "    \"username\": \"Mike\",\n" +
            "    \"first_name\": null,\n" +
            "    \"last_name\": null,\n" +
            "    \"email\": \"mike@example.com\",\n" +
            "    \"phone\": null,\n" +
            "    \"createDate\": \"2023-05-02T10:41:30.200507+03:00\",\n" +
            "    \"lastUpdateDate\": \"2023-05-02T10:41:30.200507+03:00\",\n" +
            "    \"roles\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"ROLE_SUBSCRIBER\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 2,\n" +
            "        \"name\": \"ROLE_JOURNALIST\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 3,\n" +
            "        \"name\": \"ROLE_ADMIN\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"status\": \"ACTIVE\"\n" +
            "  }\n" +
            "}\n";
}
