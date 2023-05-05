package ru.clevertec.ecl.authservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.authservice.model.enums.Status;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "User DTO Response")
public class UserDtoResponse implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "createDate")
    private OffsetDateTime createDate;

    @JsonProperty(value = "lastUpdateDate")
    private OffsetDateTime lastUpdateDate;

    @JsonProperty(value = "roles")
    private List<RoleDtoResponse> roles;

    @JsonProperty(value = "status")
    private Status status;
}
