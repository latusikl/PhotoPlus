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
    @OnlyLetters(message = "Only letters, space and dash are allowed in street field.")
    @Length(min = 4, max = 20, message = "Street should be longer than 3 and shorter than 21 signs.")
    @JsonProperty("street")
    @Patchable
    private String street;

    @NotBlank(message = "Number is mandatory.")
    @Pattern(regexp = "\\p{Alnum}{1,4}[/]?\\p{Alnum}{0,4}", message = "Please check your apartment number.")
    @JsonProperty("number")
    @Patchable
    private String number;

    @NotBlank
    @Pattern(regexp = "\\d{2,5}-?\\d{2,5}", message = "Bad zipcode.")
    @JsonProperty("zipCode")
    @Patchable
    private String zipCode;

    @NotBlank(message = "City cannot be empty.")
    @OnlyLetters(message = "Only letters, space and dash are allowed in city field.")
    @Length(min = 2, max = 20, message = "City should be longer than 1 and shorter than 21 signs.")
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
