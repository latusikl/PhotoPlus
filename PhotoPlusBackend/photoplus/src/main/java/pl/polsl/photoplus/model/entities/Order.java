package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "orders")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends AbstractEntityModel {

    public Order(final String orderStatus, final String paymentMethod, final Integer price) {
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    @ManyToOne
    private User user;

    @Patchable
    private String orderStatus;

    @Patchable
    private String paymentMethod;

    @Patchable
    private Integer price;

}

