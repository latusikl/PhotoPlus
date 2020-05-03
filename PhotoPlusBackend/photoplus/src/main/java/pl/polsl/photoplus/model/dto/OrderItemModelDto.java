package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderItemModelDto
        extends AbstractModelDto<OrderItemModelDto>
{
    @JsonProperty("orderCode")
    private String orderCode;

    @NotBlank(message = "Product code is mandatory.")
    @JsonProperty("productCode")
    private String productCode;

    @NotNull(message = "Quantity is mandatory.")
    @JsonProperty("quantity")
    @Patchable
    private Integer quantity;

    public OrderItemModelDto(final String code, final String productCode, final String orderCode, final Integer quantity)
    {
        super(code);
        this.productCode = productCode;
        this.orderCode = orderCode;
        this.quantity = quantity;
    }

}
