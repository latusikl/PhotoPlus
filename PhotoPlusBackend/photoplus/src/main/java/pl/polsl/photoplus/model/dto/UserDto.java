package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@JsonPropertyOrder({"login", "password", "name", "surname", "email"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserDto extends AbstractDto
{

    @JsonProperty("login")
    @NotBlank(message = "Login is mandatory.")
    private String login;

    @JsonProperty("email")
    @Email(message = "Email address is taken or not valid.")
    private String email;

    @JsonProperty("name")
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @JsonProperty("surname")
    @NotBlank(message = "Surname is mandatory.")
    private String surname;

    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @JsonProperty("number")
    @Pattern(regexp = "[1-9][0-9]{2}-[0-9]{3}-[0-9]{3}", message = "Please add valid phone number.")
    private String number;
}

