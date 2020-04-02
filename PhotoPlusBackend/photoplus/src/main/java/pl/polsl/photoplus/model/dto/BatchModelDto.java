package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
public class BatchModelDto extends AbstractModelDto<BatchModelDto> {

    public BatchModelDto(final String code, final String productCode, final Integer purchasePrice,
                         final LocalDate date, final Integer supplyQuantity, final Integer storeQuantity) {
        super(code);
        this.productCode = productCode;
        this.purchasePrice = purchasePrice;
        this.date = date;
        this.supplyQuantity = supplyQuantity;
        this.storeQuantity = storeQuantity;
    }

    @NotBlank(message = "Product code is mandatory.")
    private String productCode;

    @NotNull(message = "Purchase price is mandatory.")
    private Integer purchasePrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    private LocalDate date;

    @NotNull(message = "Supply quantity is mandatory.")
    private Integer supplyQuantity;

    @NotNull(message = "Store quantity is mandatory.")
    private Integer storeQuantity;

}
