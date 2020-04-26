package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Post;
import pl.polsl.photoplus.model.entities.Topic;

import java.util.List;

@Repository
public interface PostRepository extends EntityRepository<Post, Long> {
    List<Post> findAllByTopic_Code(final String code);
}
