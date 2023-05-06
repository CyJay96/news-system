package ru.clevertec.ecl.authservice.security.jwt;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.clevertec.ecl.authservice.model.entity.Role;
import ru.clevertec.ecl.authservice.model.entity.User;
import ru.clevertec.ecl.authservice.model.enums.Status;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtUser factory to work with UserDetails
 *
 * @author Konstantin Voytko
 */
@NoArgsConstructor
public final class JwtUserFactory {

    /**
     * Create a new JwtUser
     *
     * @param user User entity to create JwtUser
     * @return created JwtUser
     */
    public static JwtUser create(final User user) {
        return JwtUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.getStatus().equals(Status.ACTIVE))
                .lastPasswordResetDate(user.getLastUpdateDate())
                .authorities(mapToGrantedAuthorities(user.getRoles()))
                .build();
    }

    /**
     * Map User roles to GrantedAuthorities
     *
     * @param userRoles User roles to map
     * @return User GrantedAuthorities
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(final List<Role> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
