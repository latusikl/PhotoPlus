package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
    List<Product> getAllByCategory_Code(final String code);
}
