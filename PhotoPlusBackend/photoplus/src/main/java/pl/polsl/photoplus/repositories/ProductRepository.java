package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
    List<Product> findAllByCategory_CodeOrderByName(final String code);
    Page<Product> findAllByCategory_CodeAndStoreQuantityGreaterThanOrderByName(Pageable page, final String code, Integer storeQuantity);
    List<Product> findAllByNameContainingIgnoreCaseOrderByName(final String str);
    List<Product> findAllByNameContainingIgnoreCaseAndStoreQuantityGreaterThanOrderByName(final String str, Integer storeQuantity);
    List<Product> findAllByOrderByName();
    List<Product> findTop8ByStoreQuantityGreaterThan(Integer storeQuantity);
    Page<Product> findAllByStoreQuantityGreaterThanOrderByName(Pageable page, Integer storeQuantity);
    Page<Product> findAllByStoreQuantityGreaterThanOrderByPriceAsc(Pageable page, Integer storeQuantity);
    Page<Product> findAllByStoreQuantityGreaterThanOrderByPriceDesc(Pageable page, Integer storeQuantity);
}
