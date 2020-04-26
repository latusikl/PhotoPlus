package pl.polsl.photoplus.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import pl.polsl.photoplus.security.custom.CustomPermissionEvaluator;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig
        extends GlobalMethodSecurityConfiguration
{

    final CustomPermissionEvaluator customPermissionEvaluator;

    public MethodSecurityConfig(final CustomPermissionEvaluator customPermissionEvaluator)
    {
        this.customPermissionEvaluator = customPermissionEvaluator;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler()
    {
        final DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator);
        return expressionHandler;
    }
}