package ru.clevertec.ecl.newsservice.client.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.newsservice.client.model.enums.Status;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
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
