package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Section;

@Repository
public interface SectionRepository extends EntityRepository<Section, Long> {
}
