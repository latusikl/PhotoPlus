package pl.polsl.photoplus.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User
        extends AbstractEntityModel
{

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

    @Column(name = "phone_number")
    @Patchable
    private String number;
}
