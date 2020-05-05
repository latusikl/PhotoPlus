package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Batch;

import java.util.List;

@Repository
public interface BatchRepository extends EntityRepository<Batch, Long> {
    List<Batch> getAllByProduct_CodeOrderByDate(final String code);
}
