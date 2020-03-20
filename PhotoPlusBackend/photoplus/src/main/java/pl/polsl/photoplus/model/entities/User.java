package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.polsl.photoplus.services.annotations.Patchable;

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
        extends AbstractEntityModel
{

    @Column(name = "phone_number")
    @Pattern(regexp = "[1-9][0-9]{2}-[0-9]{3}-[0-9]{3}") @NotBlank
    @Patchable
    String number;

    @Column(unique = true, name = "login")
    @NotBlank(message = "Login cannot be empty.")
    @Length(min = 5,message = "Login should be longer than 5 signs.")
    @Patchable
    private String login;

    @Column(unique = true, name = "email")
    @Email(message = "Email is invalid.")
    @NotBlank(message = "Email is mandatory.")
    @Patchable
    private String email;

    @Column(name = "user_name")
    @NotBlank(message = "Name cannot be empty.")
    @Patchable
    private String name;

    @Column(name = "user_surname")
    @NotBlank(message = "Surname cannot be empty.")
    @Patchable
    private String surname;

    @Column(name = "password")
    @NotBlank(message = "Password cannot be empty.")
    @Patchable
    private String password;

    @Override
    protected void generateModelCode()
    {
        code = login.substring(0, CODE_NAME_LENGTH) + createCodeNumber();
    }

    public User(final String login, final String email, final String name,final String surname, final String password, final String number)
    {
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.number = number;
    }

    public void setLogin(final String login){
        this.login=login;
        generateModelCode();
    }

    public User()
    {
    }
}
