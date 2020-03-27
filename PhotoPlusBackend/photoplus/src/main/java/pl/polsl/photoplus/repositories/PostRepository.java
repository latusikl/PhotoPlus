package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Post;

@Repository
public interface PostRepository extends EntityRepository<Post, Long> {
}
