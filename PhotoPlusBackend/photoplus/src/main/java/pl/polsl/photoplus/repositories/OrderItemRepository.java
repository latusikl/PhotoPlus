package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.OrderItem;

@Repository
public interface OrderItemRepository extends EntityRepository<OrderItem, Long> {
}
