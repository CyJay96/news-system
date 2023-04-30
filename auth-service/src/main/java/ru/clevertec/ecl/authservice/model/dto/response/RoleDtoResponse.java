package ru.clevertec.ecl.authservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RoleDtoResponse implements Serializable {

    private Long id;

    private String name;
}
