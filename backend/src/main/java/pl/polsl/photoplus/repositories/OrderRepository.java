package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Order;

@Repository
public interface OrderRepository extends EntityRepository<Order, Long> {
}
