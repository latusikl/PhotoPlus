package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
public class User
        extends AbstractModel
{

    @Column(name = "phone_number")
    @Pattern(regexp = "[1-9][0-9]{2}-[0-9]{3}-[0-9]{3}") @NotBlank String number;

    @Column(unique = true, name = "login")
    @NotBlank(message = "Login cannot be empty.")
    private String login;

    @Column(unique = true, name = "email")
    @Email(message = "Email is invalid.")
    private String email;

    @Column(name = "user_name")
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @Column(name = "user_surname")
    @NotBlank(message = "Surname cannot be empty.")
    private String surname;

    @Column(name = "password")
    @NotBlank(message = "Password cannot be empty.")
    private String password;

    @Override
    protected void generateModelCode()
    {
        code = login.substring(0, CODE_NAME_LENGTH) + createCodeNumber();
    }
}
