package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RatingModelDto extends AbstractModelDto<RatingModelDto> {

    public RatingModelDto(final String code, final String userCode, final String productCode, final Integer rate,
                          final String content) {
        super(code);
        this.userCode = userCode;
        this.productCode = productCode;
        this.rate = rate;
        this.content = content;
    }

    @NotBlank(message = "User code is mandatory.")
    private String userCode;

    @NotBlank(message = "Product code is mandatory.")
    private String productCode;

    @NotNull(message = "Rate is mandatory.")
    @Min(0)
    @Max(5)
    private Integer rate;

    private String content;

}
