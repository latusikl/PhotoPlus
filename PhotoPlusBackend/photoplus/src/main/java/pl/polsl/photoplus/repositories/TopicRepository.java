package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Topic;

@Repository
public interface TopicRepository extends EntityRepository<Topic, Long> {
}
