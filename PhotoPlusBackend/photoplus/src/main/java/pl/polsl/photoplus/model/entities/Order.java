package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "orders")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends AbstractEntityModel {

    public Order(final OrderStatus orderStatus, final PaymentMethod paymentMethod, final Double price,
                 final LocalDate date) {
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.date = date;
    }

    @ManyToOne
    private User user;

    @ManyToOne
    private Address address;

    @Patchable
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Patchable
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Patchable
    private Double price;

    @Patchable
    private LocalDate date;

}

