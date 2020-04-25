package pl.polsl.photoplus.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import pl.polsl.photoplus.security.custom.CustomPermissionEvaluator;
import pl.polsl.photoplus.security.services.RolePropertiesService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final RolePropertiesService rolePropertiesService;

    public MethodSecurityConfig(final RolePropertiesService rolePropertiesService){
        super();
        this.rolePropertiesService = rolePropertiesService;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        final DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(rolePropertiesService));
        return expressionHandler;
    }
}