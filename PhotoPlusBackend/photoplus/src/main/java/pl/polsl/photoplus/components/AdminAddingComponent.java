package pl.polsl.photoplus.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.UserRole;
import pl.polsl.photoplus.repositories.UserRepository;

import java.util.Optional;

@Component
@Slf4j
public class AdminAddingComponent
{
    private final UserRepository userRepository;

    public AdminAddingComponent(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addAdmin()
    {
        final Optional<User> adminUser = userRepository.findUserByLogin("admin");
        if (adminUser.isEmpty()) {
            final User admin = new User("admin", "admin@admin.admin", "Admin", "Admin", "nimda", "555-555-555", UserRole.ADMIN);
            userRepository.save(admin);
            log.info("Added admin user to database.");
        }
    }
}
