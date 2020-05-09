package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.annotations.validators.OnlyLetters;
import pl.polsl.photoplus.annotations.validators.Unique;
import pl.polsl.photoplus.annotations.validators.ValueOfEnum;
import pl.polsl.photoplus.model.enums.UserRole;
import pl.polsl.photoplus.services.controllers.UserService;

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
    @Unique(service = UserService.class, fieldName = "login", fieldNameToBeDisplayed = "Login")
    private String login;

    @JsonProperty("email")
    @Email(message = "Email address is taken or not valid.")
    @NotBlank(message = "Email is mandatory.")
    @Unique(service = UserService.class, fieldName = "email", fieldNameToBeDisplayed = "E-mail address")
    private String email;

    @JsonProperty("name")
    @NotBlank(message = "Name is mandatory.")
    @OnlyLetters(message = "Invalid name. Only letters are allowed.")
    @Patchable
    private String name;

    @JsonProperty("surname")
    @NotBlank(message = "Surname is mandatory.")
    @OnlyLetters(message = "Invalid surname. Only letters are allowed.")
    @Patchable
    private String surname;

    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory.")
    @Patchable
    private String password;

    @JsonProperty("number")
    @Pattern(regexp = "(^$)|([1-9][0-9]{2}-[0-9]{3}-[0-9]{3})", message = "Please add valid phone number.")
    @Patchable
    private String number;

    @JsonProperty("role")
    @ValueOfEnum(enumClass = UserRole.class)
    @Patchable(method = "userRolePatch")
    private String userRole;

    public UserModelDto(final String login, final String email, final String name, final String surname, final String password, final String number, final String code, final String userRole)
    {
        super(code);
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.number = number;
        this.userRole = userRole != null ? userRole : "CLIENT";
    }

    @JsonIgnore
    public UserRole userRolePatch()
    {
        return UserRole.getUserRoleFromString(this.userRole);
    }

}

