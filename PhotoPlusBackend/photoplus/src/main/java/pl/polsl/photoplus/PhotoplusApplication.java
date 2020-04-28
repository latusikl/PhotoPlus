package pl.polsl.photoplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PhotoplusApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PhotoplusApplication.class, args);
    }

}
