package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends EntityRepository<User,Long>
{
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);
    List<User> findByLoginContainingIgnoreCase(String str);
    List<User> findAllByOrderByLogin();
    Page<User> findAllByOrderByLogin(Pageable page);
}
