package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderModelDtoWithOrderItems extends OrderModelDto {

    public OrderModelDtoWithOrderItems(final String code, final String userCode, final String addressCode, final String orderStatus,
                                       final String paymentMethod, final Double price, final LocalDate date) {
        super(code, userCode, addressCode, orderStatus, paymentMethod, price, date);
    }

    @JsonProperty("orderItems")
    @NotEmpty(message = "Order item list shouldn't be empty.")
    @Valid
    private List<OrderItemModelDto> orderItemModelDtos;
}