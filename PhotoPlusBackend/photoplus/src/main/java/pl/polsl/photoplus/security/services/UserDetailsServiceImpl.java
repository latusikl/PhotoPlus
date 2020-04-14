package pl.polsl.photoplus.security.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.UserRepository;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl
        implements UserDetailsService
{
    final UserRepository userRepository;

    public UserDetailsServiceImpl(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException
    {
        final Optional<User> foundUser = userRepository.findUserByLogin(login);
        if (foundUser.isEmpty()) {
            log.warn("Authentication for user: {} failed. User not found.", login);
            throw new UsernameNotFoundException("User with login: " + login + "not found.");
        }
        return foundUser.get();
    }
}
