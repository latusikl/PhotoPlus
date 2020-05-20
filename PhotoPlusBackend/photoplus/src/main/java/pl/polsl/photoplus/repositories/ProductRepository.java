package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
    Page<Product> findAllByCategory_CodeAndStoreQuantityGreaterThan(final Pageable page, final String code, final Integer storeQuantity);
    Page<Product> findAllByNameContainingIgnoreCaseAndStoreQuantityGreaterThan(final Pageable page, final String str, final Integer storeQuantity);
    Page<Product> findAllByNameContainingIgnoreCase(final Pageable page, final String str);
    List<Product> findTop8ByStoreQuantityGreaterThan(final Integer storeQuantity);
    Page<Product> findAllByStoreQuantityGreaterThan(final Pageable page, final Integer storeQuantity);
}
