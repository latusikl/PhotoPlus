package pl.polsl.photoplus.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "photoplus")
@Getter
@Setter
public class ModelPropertiesService
{
    public Integer pageSize;

}
