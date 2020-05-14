package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.enums.OrderStatus;

@Repository
public interface OrderRepository extends EntityRepository<Order, Long> {
    Page<Order> findAllByOrOrderStatus(Pageable page, final OrderStatus orderStatus);
}
