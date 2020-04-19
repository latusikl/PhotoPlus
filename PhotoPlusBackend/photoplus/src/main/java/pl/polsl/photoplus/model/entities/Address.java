package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.annotations.validators.CountryCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity(name = "addresses")
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address
        extends AbstractEntityModel
{

    @Patchable
    private String street;

    @Patchable
    private String number;

    @Patchable
    private String zipCode;

    @Patchable
    private String city;

    @CountryCode
    @Patchable
    private String countryCode;

    @ManyToOne
    private User addressOwner;

    public Address(final String street, final String number, final String zipCode, final String city, final String countryCode)
    {
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
        this.countryCode = countryCode;
    }

    public String getOwnerCode()
    {
        return addressOwner.getCode();
    }
}
