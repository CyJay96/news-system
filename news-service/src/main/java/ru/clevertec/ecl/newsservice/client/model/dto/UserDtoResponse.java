package ru.clevertec.ecl.newsservice.client.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.newsservice.client.model.enums.Status;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO Response")
public class UserDtoResponse implements Serializable {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private OffsetDateTime createDate;

    private OffsetDateTime lastUpdateDate;

    private List<RoleDtoResponse> roles;

    private Status status;
}
