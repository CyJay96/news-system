package ru.clevertec.ecl.newsservice.model.dto.response;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

@Value
@Builder
public class ApiResponse<T> {

    OffsetDateTime timestamp = OffsetDateTime.now();

    int status;

    String message;

    String path;

    T data;

    public static <T> ResponseEntity<ApiResponse<T>> of(
            final String message,
            final String path,
            final HttpStatus httpStatus,
            final T body
    ) {
        final ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .message(message)
                .path(path)
                .status(httpStatus.value())
                .data(body)
                .build();

        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}
