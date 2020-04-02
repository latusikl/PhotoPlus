package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Batch;

@Repository
public interface BatchRepository extends EntityRepository<Batch, Long> {
}
