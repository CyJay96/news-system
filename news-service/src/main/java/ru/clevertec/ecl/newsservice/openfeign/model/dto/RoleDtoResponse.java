package ru.clevertec.ecl.newsservice.openfeign.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RoleDtoResponse implements Serializable {

    private Long id;

    private String name;
}
