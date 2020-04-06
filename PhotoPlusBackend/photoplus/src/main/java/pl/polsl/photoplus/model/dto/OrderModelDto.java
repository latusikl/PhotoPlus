package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.annotations.validators.ValueOfEnum;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderModelDto
        extends AbstractModelDto<BatchModelDto>
{
    @NotBlank(message = "User code is mandatory.")
    @JsonProperty("userCode")
    private String userCode;

    @NotBlank(message = "Order status is mandatory.")
    @JsonProperty("orderStatus")
    @ValueOfEnum(enumClass = OrderStatus.class)
    @Patchable(method = "orderStatusPatch")
    private String orderStatus;

    @NotBlank(message = "Payment method is mandatory.")
    @JsonProperty("paymentMethod")
    @ValueOfEnum(enumClass = PaymentMethod.class)
    @Patchable(method = "paymentMethodPatch")
    private String paymentMethod;

    @NotNull(message = "Price is mandatory.")
    @JsonProperty("price")
    @Patchable
    private Integer price;

    public OrderModelDto(final String code, final String userCode, final String orderStatus, final String paymentMethod, final Integer price)
    {
        super(code);
        this.userCode = userCode;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    public PaymentMethod paymentMethodPatch()
    {
       return PaymentMethod.getPaymentMethodFromString(this.paymentMethod);
    }

    public OrderStatus orderStatusPatch()
    {
        return OrderStatus.getOrderStatusFromString(this.orderStatus);
    }
}
