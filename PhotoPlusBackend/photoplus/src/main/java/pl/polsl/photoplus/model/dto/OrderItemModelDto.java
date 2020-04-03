package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.validators.ValueOfEnum;

import javax.persistence.Enumerated;
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

    @NotBlank(message = "Order code is mandatory.")
    @JsonProperty("orderCode")
    private String orderCode;

    @NotBlank(message = "Product code is mandatory.")
    @JsonProperty("productCode")
    private String productCode;

    @NotNull(message = "Quantity is mandatory.")
    @JsonProperty("quantity")
    private Integer quantity;

}
