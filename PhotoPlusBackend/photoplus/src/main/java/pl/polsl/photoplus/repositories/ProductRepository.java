package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
    Page<Product> findAllByCategory_CodeAndStoreQuantityGreaterThan(Pageable page, final String code, Integer storeQuantity);
    List<Product> findAllByNameContainingIgnoreCaseAndStoreQuantityGreaterThanOrderByName(final String str, Integer storeQuantity);
    List<Product> findTop8ByStoreQuantityGreaterThan(Integer storeQuantity);
    Page<Product> findAllByStoreQuantityGreaterThan(Pageable page, Integer storeQuantity);
}
