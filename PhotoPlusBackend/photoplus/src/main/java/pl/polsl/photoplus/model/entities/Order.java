package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;

import javax.persistence.*;

@Entity(name = "orders")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends AbstractEntityModel {

    public Order(final OrderStatus orderStatus, final PaymentMethod paymentMethod, final Integer price) {
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    @ManyToOne
    private User user;

    @Patchable
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Patchable
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Patchable
    private Integer price;

}

