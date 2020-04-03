package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User
        extends AbstractEntityModel
{

    @Column(name = "phone_number")
    @Patchable
    private String number;

    @OneToMany(mappedBy = "addressOwner", cascade = CascadeType.ALL)
    private Set<Address> userAddresses;

    @Column(unique = true, name = "login")
    @Patchable
    private String login;

    @Column(unique = true, name = "email")
    @Patchable
    private String email;

    @Column(name = "user_name")
    @Patchable
    private String name;

    @Column(name = "user_surname")
    @Patchable
    private String surname;

    @Column(name = "password")
    @Patchable
    private String password;

    public User(final String login, final String email, final String name, final String surname, final String password, final String number)
    {
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.number = number;
    }
}
