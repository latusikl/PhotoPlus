package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.annotations.validators.CountryCode;
import pl.polsl.photoplus.annotations.validators.OnlyLetters;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressModelDto
        extends AbstractModelDto<AddressModelDto>
{

    @NotBlank(message = "Street is mandatory.")
    @OnlyLetters(message = "Only letters are allowed in street field.")
    @Length(min = 4, message = "Street name is to short.")
    @JsonProperty("street")
    @Patchable
    private String street;

    @NotBlank(message = "Number is mandatory.")
    @Pattern(regexp = "\\p{Alnum}+", message = "Only letters and numbers are allowed in number field.")
    @JsonProperty("number")
    @Patchable
    private String number;

    @NotBlank
    @Pattern(regexp = "\\d{2,5}-?\\d{2,5}", message = "Bad zipcode.")
    @JsonProperty("zipCode")
    @Patchable
    private String zipCode;

    @NotBlank(message = "City cannot be empty.")
    @OnlyLetters(message = "Only letters are allowed in city field.")
    @JsonProperty("city")
    @Patchable
    private String city;

    @CountryCode
    @JsonProperty("country")
    @Patchable
    private String countryCode;

    @NotBlank(message = "User code cannot be empty")
    @JsonProperty(value = "userCode")
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
