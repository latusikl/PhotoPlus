package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends EntityRepository<Topic, Long> {
    List<Topic> findAllBySection_Code(final String code);
}
