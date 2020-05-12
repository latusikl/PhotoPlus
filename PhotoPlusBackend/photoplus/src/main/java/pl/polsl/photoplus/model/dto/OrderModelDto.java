package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.annotations.validators.ValueOfEnum;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
public class OrderModelDto
        extends AbstractModelDto<OrderModelDto>
{
    @NotBlank(message = "User code is mandatory.")
    @JsonProperty("userCode")
    private String userCode;

    @NotBlank(message = "Address code is mandatory.")
    @JsonProperty("addressCode")
    private String addressCode;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    @JsonProperty("date")
    @Patchable
    private LocalDate date;

    @JsonProperty("price")
    @Patchable
    private Double price;

    public OrderModelDto(final String code, final String userCode, final String addressCode, final String orderStatus, final String paymentMethod,
                         final Double price, final LocalDate date)
    {
        super(code);
        this.userCode = userCode;
        this.addressCode = addressCode;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.date = date;
    }

    @JsonIgnore
    public PaymentMethod paymentMethodPatch()
    {
       return PaymentMethod.getPaymentMethodFromString(this.paymentMethod);
    }

    @JsonIgnore
    public OrderStatus orderStatusPatch()
    {
        return OrderStatus.getOrderStatusFromString(this.orderStatus);
    }
}
