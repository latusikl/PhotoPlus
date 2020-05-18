package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends EntityRepository<Category, Long> {
    List<Category> findAllByOrderByName();
}
