package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderItemModelDto extends AbstractModelDto<OrderItemModelDto> {
    public OrderItemModelDto(final String code, final String productCode, final String orderCode, final Integer quantity) {
        super(code);
        this.productCode = productCode;
        this.orderCode = orderCode;
        this.quantity = quantity;
    }

    @NotBlank(message = "User code is mandatory.")
    private String orderCode;

    @NotBlank(message = "User code is mandatory.")
    private String productCode;

    @NotNull(message = "Quantity is mandatory.")
    private Integer quantity;

}
