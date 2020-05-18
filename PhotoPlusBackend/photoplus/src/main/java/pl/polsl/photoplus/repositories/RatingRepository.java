package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends EntityRepository<Rating, Long> {
    List<Rating> getAllByProduct_Code(final String productCode);
    Page<Rating> findAllByProduct_Code(Pageable page, final String productCode);
}
