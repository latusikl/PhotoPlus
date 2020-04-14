package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository
        extends EntityRepository<User,Long>
{
    Optional<User> findUserByLogin(String login);
}
