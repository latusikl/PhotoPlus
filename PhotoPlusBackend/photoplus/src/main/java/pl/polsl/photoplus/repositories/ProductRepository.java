package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Long> {
    List<Product> getAllByCategory_Code(final String code);
    List<Product> findByNameContainingIgnoreCase(String str);
    List<Product> findAllByOrderByName();
    Page<Product> findAllByOrderByName(Pageable page);
    Page<Product> findAllByOrderByPriceAsc(Pageable page);
    Page<Product> findAllByOrderByPriceDesc(Pageable page);
}
