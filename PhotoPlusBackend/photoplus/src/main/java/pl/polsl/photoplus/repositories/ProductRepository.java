package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
    List<Product> findAllByCategory_CodeOrderByName(final String code);
    Page<Product> findAllByCategory_CodeOrderByName(Pageable page, final String code);
    List<Product> findAllByNameContainingIgnoreCaseOrderByName(final String str);
    List<Product> findAllByOrderByName();
    List<Product> findTop8ByStoreQuantityGreaterThan(Integer storeQuantity);
    Page<Product> findAllByOrderByName(Pageable page);
    Page<Product> findAllByOrderByPriceAsc(Pageable page);
    Page<Product> findAllByOrderByPriceDesc(Pageable page);
}
