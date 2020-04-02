package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderModelDto extends AbstractModelDto<BatchModelDto> {
    public OrderModelDto(final String code, final String userCode, final String orderStatus,
                         final String paymentMethod, final Integer price) {
        super(code);
        this.userCode = userCode;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    @NotBlank(message = "User code is mandatory.")
    private String userCode;

    @NotBlank(message = "User code is mandatory.")
    private String orderStatus;

    @NotBlank(message = "Payment method is mandatory.")
    private String paymentMethod;

    @NotNull(message = "Price is mandatory.")
    private Integer price;
}
