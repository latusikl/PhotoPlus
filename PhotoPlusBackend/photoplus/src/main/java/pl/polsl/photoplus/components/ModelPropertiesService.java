package pl.polsl.photoplus.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class stores common properties for application,
 * Properties are defined in application.yaml and injected here by Spring.
 */
@Configuration
@ConfigurationProperties(prefix = "photoplus")
@Getter
@Setter
public class ModelPropertiesService
{
    public Integer pageSize;

    public String securitySecret;

    public Integer securityExpiration;

    public String securityHeaderName;

    public String securityTokenPrefix;

}
