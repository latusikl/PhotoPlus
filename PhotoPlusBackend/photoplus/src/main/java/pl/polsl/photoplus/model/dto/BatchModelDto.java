package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.polsl.photoplus.annotations.Patchable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
public class BatchModelDto extends AbstractModelDto<BatchModelDto> {

    public BatchModelDto(final String code, final String productCode, final Double purchasePrice,
                         final LocalDate date, final Integer supplyQuantity, final Integer storeQuantity) {
        super(code);
        this.productCode = productCode;
        this.purchasePrice = purchasePrice;
        this.date = date;
        this.supplyQuantity = supplyQuantity;
        this.storeQuantity = storeQuantity;
    }

    @NotBlank(message = "Product code is mandatory.")
    @JsonProperty("productCode")
    private String productCode;

    @NotNull(message = "Purchase price is mandatory.")
    @JsonProperty("purchasePrice")
    @Patchable
    private Double purchasePrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    @JsonProperty("date")
    @Patchable
    private LocalDate date;

    @NotNull(message = "Supply quantity is mandatory.")
    @JsonProperty("supplyQuantity")
    @Patchable
    private Integer supplyQuantity;

    @NotNull(message = "Store quantity is mandatory.")
    @JsonProperty("storeQuantity")
    @Patchable
    private Integer storeQuantity;

}
