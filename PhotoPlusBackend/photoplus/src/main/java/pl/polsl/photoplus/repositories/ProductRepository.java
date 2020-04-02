package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
}
