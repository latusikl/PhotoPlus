package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.enums.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository
        extends EntityRepository<Order,Long>
{
    Page<Order> findAllByOrderStatus(Pageable page, final OrderStatus orderStatus);

    @Query("SELECT o.code FROM orders as o WHERE o.user.code = ?1")
    List<String> findAllBaseInfoByUser(String userCode);
}
