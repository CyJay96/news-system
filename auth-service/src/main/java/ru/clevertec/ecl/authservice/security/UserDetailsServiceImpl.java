package ru.clevertec.ecl.authservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.authservice.model.entity.User;
import ru.clevertec.ecl.authservice.security.jwt.JwtUserFactory;
import ru.clevertec.ecl.authservice.service.UserService;

/**
 * UserDetails Service to work with the UserDetails
 *
 * @author Konstantin Voytko
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    /**
     * Load UserDetails by username
     *
     * @param username username to load UserDetails
     * @throws UsernameNotFoundException if the Username was not found
     * @return loaded UserDetails by username
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findEntityByUsername(username);
        return JwtUserFactory.create(user);
    }
}
