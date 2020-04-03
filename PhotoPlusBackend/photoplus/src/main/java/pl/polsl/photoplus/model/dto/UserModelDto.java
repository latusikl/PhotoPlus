package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.polsl.photoplus.annotations.validators.OnlyLetters;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@JsonPropertyOrder({"login", "password", "name", "surname", "email"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserModelDto
        extends AbstractModelDto<UserModelDto>
{

    @JsonProperty("login")
    @NotBlank(message = "Login is mandatory.")
    @Length(min = 5, message = "Login should be longer than 5 signs.")
    private String login;

    @JsonProperty("email")
    @Email(message = "Email address is taken or not valid.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @JsonProperty("name")
    @NotBlank(message = "Name is mandatory.")
    @OnlyLetters
    private String name;

    @JsonProperty("surname")
    @NotBlank(message = "Surname is mandatory.")
    @OnlyLetters
    private String surname;

    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @JsonProperty("number")
    @Pattern(regexp = "[1-9][0-9]{2}-[0-9]{3}-[0-9]{3}", message = "Please add valid phone number.")
    private String number;

    public UserModelDto(final String login, final String email, final String name, final String surname, final String password, final String number, final String code)
    {
        super(code);
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.number = number;
    }
}

