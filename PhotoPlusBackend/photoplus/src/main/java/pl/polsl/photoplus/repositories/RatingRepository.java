package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Rating;

@Repository
public interface RatingRepository extends EntityRepository<Rating, Long> {
}
