package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.components.ContextProvider;
import pl.polsl.photoplus.model.enums.UserRole;
import pl.polsl.photoplus.security.services.RolePropertiesService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User
        extends AbstractEntityModel
        implements UserDetails
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
    @Patchable(method = "addEncoded")
    private String password;

    @Column(name = "phone_number")
    @Patchable
    private String number;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Patchable
    private UserRole userRole;

    @Column(name = "is_enabled")
    @ColumnDefault("TRUE")
    private boolean isEnabled = true;

    public User(final String login, final String email, final String name, final String surname, final String password, final String number, final UserRole userRole)
    {
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = encodePassword(password);
        this.number = number;
        this.userRole = userRole;
    }

    public void addEncoded(final String password)
    {
        final PasswordEncoder passwordEncoder = ContextProvider.getBean(PasswordEncoder.class, "encoder");
        this.password = passwordEncoder.encode(password);
    }

    public String encodePassword(final String password)
    {
        final PasswordEncoder passwordEncoder = ContextProvider.getBean(PasswordEncoder.class, "encoder");
        return passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        final RolePropertiesService rolePropertiesService = ContextProvider.getBean(RolePropertiesService.class);
        final List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        final String userRoleValue = userRole.getValue();
        if (rolePropertiesService.getRoleNames().contains(userRoleValue)) {
            final List<String> roleAuthority = rolePropertiesService.getPropertiesByRoleName(userRoleValue);
            roleAuthority.forEach(authority -> grantedAuthorityList.add(new SimpleGrantedAuthority(authority)));
        }
        return grantedAuthorityList;
    }

    @Override
    public String getUsername()
    {
        return login;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return this.isEnabled;
    }
}
