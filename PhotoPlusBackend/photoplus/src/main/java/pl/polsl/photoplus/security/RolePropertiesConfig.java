package pl.polsl.photoplus.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polsl.photoplus.security.services.InputException;
import pl.polsl.photoplus.security.services.RolePropertiesService;

@Configuration
public class RolePropertiesConfig {

    @Bean
    public RolePropertiesService createRolePropertiesService() throws InputException {
        final RolePropertiesService service = new RolePropertiesService();
        service.loadRoleConfig();
        return service;
    }
}
