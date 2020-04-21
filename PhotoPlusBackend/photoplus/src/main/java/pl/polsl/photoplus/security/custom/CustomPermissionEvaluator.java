package pl.polsl.photoplus.security.custom;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private static final List<String> PUBLIC_LINK_WHITELIST = List.of(
            "product/all",
            "category/all", "category/single");

    @Override
    public boolean hasPermission(final Authentication auth, final Object targetDomainObject, final Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }

        return hasPrivilege(auth, targetDomainObject.toString(), permission.toString());
    }

    @Override
    public boolean hasPermission(final Authentication auth, final Serializable targetId, final String targetType,
                                 final Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(auth, targetType, permission.toString());
    }

    private boolean hasPrivilege(final Authentication auth, final String targetType, final String permission) {
        final String urlToAccess = targetType + "/" + permission;
        for (final GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().equalsIgnoreCase(urlToAccess) || PUBLIC_LINK_WHITELIST.contains(urlToAccess)) {
                    return true;
            }
        }
        return false;
    }
}
