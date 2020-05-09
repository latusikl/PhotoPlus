package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RatingModelDto
        extends AbstractModelDto<RatingModelDto>
{

    @NotBlank(message = "User code is mandatory.")
    @JsonProperty("userCode")
    private String userCode;

    @JsonProperty("userLogin")
    private String userLogin;

    @NotBlank(message = "Product code is mandatory.")
    @JsonProperty("productCode")
    private String productCode;

    @NotNull(message = "Rate is mandatory.")
    @Min(0)
    @Max(5)
    @JsonProperty("rate")
    @Patchable
    private Integer rate;

    @JsonProperty("content")
    @Patchable
    private String content;

    public RatingModelDto(final String code, final String userCode, final String productCode, final Integer rate,
                          final String content, final String userLogin)
    {
        super(code);
        this.userCode = userCode;
        this.productCode = productCode;
        this.rate = rate;
        this.content = content;
        this.userLogin = userLogin;
    }

}
