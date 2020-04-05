package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.User;

@Repository
public interface UserRepository extends EntityRepository<User,Long>
{
}
