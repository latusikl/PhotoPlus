package pl.polsl.photoplus.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionEvaluatorService {

    private final RolePropertiesService rolePropertiesService;
    private List<String> publicLinkWhitelist;
    private final UserRepository userRepository;

    public PermissionEvaluatorService(final RolePropertiesService rolePropertiesService,
                                      final UserRepository userRepository) {
        this.rolePropertiesService = rolePropertiesService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void injectWhitelistedEndpoints() {
        this.publicLinkWhitelist = rolePropertiesService.getPropertiesByRoleName("all");
    }

    public boolean hasPrivilege(final String url, final String permission) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String urlToAccess = "/" + url + "/" + permission;
        for (final GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().equalsIgnoreCase(urlToAccess) || publicLinkWhitelist.contains(urlToAccess)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrivilege(final String code) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> loggedUser = userRepository.findUserByLogin(auth.getPrincipal().toString());
        if (loggedUser.isPresent()) {
            return code.equals(loggedUser.get().getCode());
        }
        return false;
    }
}
