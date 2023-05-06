package ru.clevertec.ecl.authservice.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.authservice.model.dto.response.RoleDtoResponse;
import ru.clevertec.ecl.authservice.model.entity.Role;

import java.util.List;

/**
 * Mapper for Role entities & DTOs
 *
 * @author Konstantin Voytko
 */
@Mapper
public interface RoleMapper {

    RoleDtoResponse toRoleDroResponse(Role role);

    List<RoleDtoResponse> toRoleDroResponseList(List<Role> roles);
}
