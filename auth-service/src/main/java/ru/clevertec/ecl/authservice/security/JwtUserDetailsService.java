package ru.clevertec.ecl.authservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.model.entity.User;
import ru.clevertec.ecl.authservice.security.jwt.JwtUserFactory;
import ru.clevertec.ecl.authservice.service.UserService;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findEntityByUsername(username);
        return JwtUserFactory.create(user);
    }
}
