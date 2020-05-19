package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.OrderItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends EntityRepository<OrderItem, Long> {
    List<OrderItem> getAllByOrder_Code(final String code);
    Page<OrderItem> findAllByOrder_Code(Pageable page, final String code);
    OrderItem findFirstByOrder_Code(String code);
    List<OrderItem> findAllByProduct_Code(final String code);
}
