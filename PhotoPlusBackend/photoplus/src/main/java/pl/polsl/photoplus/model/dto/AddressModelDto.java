package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.polsl.photoplus.annotations.validators.CountryCode;
import pl.polsl.photoplus.annotations.validators.OnlyLetters;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@JsonPropertyOrder({})
@JsonIgnoreProperties(ignoreUnknown = false)
@Setter
@Getter
public class AddressModelDto
        extends AbstractModelDto<AddressModelDto>
{
    @NotBlank(message = "Street is mandatory.")
    @OnlyLetters
    @Length(min = 4, message = "Street name is to short.")
    @JsonProperty("street")
    private String street;

    @NotBlank(message = "Number is mandatory.")
    @Pattern(regexp = "\\p{Alnum}+", message = "Only letters and numbers are allowed.")
    @JsonProperty("number")
    private String number;

    @NotBlank
    @Pattern(regexp = "\\p{Digit}+", message = "Code should be more than 1 number.")
    @JsonProperty("zipCode")
    private String zipCode;

    @NotBlank(message = "City cannot be empty.")
    @OnlyLetters
    @JsonProperty("city")
    private String city;

    @CountryCode
    @JsonProperty("country")
    private String countryCode;

    @NotBlank(message = "User code cannot be empty")
    @JsonProperty("userCode")
    private String userCode;

    public AddressModelDto(final String code, final String street, final String number, final String zipCode, final String city, final String countryCode, final String userCode)
    {
        super(code);
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
        this.countryCode = countryCode;
        this.userCode = userCode;
    }
}