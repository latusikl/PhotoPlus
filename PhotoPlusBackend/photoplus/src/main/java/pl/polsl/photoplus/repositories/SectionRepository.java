package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Section;

import java.util.List;

@Repository
public interface SectionRepository extends EntityRepository<Section, Long> {
    List<Section> findAllByOrderByName();
}
