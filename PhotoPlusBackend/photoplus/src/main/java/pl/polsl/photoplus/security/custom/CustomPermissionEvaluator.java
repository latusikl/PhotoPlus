package pl.polsl.photoplus.security.custom;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.UserRepository;
import pl.polsl.photoplus.security.services.RolePropertiesService;
import pl.polsl.photoplus.services.controllers.UserService;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
public class CustomPermissionEvaluator
        implements PermissionEvaluator
{

    private final RolePropertiesService rolePropertiesService;
    private List<String> publicLinkWhitelist;
    private final UserRepository userRepository;

    public CustomPermissionEvaluator(final RolePropertiesService rolePropertiesService, final UserRepository userRepository)
    {
        this.rolePropertiesService = rolePropertiesService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void injectWhitelistedEndpoints()
    {
        this.publicLinkWhitelist = rolePropertiesService.getPropertiesByRoleName("all");
    }

    @Override
    public boolean hasPermission(final Authentication auth, final Object targetDomainObject, final Object permission)
    {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(auth, targetDomainObject.toString(), permission.toString());
    }

    @Override
    public boolean hasPermission(final Authentication auth, final Serializable targetId, final String targetType, final Object permission)
    {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(auth, targetType, permission.toString());

    }

    private boolean hasPrivilege(final Authentication auth, final String targetType, final String permission)
    {
        if (!permission.chars().allMatch( Character::isDigit)) {
            //doesnt require to check if logged user code == code (permission parameter)
            final String urlToAccess = "/" + targetType + "/" + permission;
            for (final GrantedAuthority grantedAuth : auth.getAuthorities()) {
                if (grantedAuth.getAuthority().equalsIgnoreCase(urlToAccess) || publicLinkWhitelist.contains(urlToAccess)) {
                    return true;
                }
            }
        } else {
            //checking if logged user code equals code from parameter
            final Optional<User> loggedUser = userRepository.findUserByLogin(auth.getPrincipal().toString());
            if (loggedUser.isPresent()) {
                return permission.equals(loggedUser.get().getCode());
            }
        }
        return false;
    }
}
