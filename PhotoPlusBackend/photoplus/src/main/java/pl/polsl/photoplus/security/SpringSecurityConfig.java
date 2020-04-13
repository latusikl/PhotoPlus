package pl.polsl.photoplus.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import pl.polsl.photoplus.components.ModelPropertiesService;
import pl.polsl.photoplus.repositories.UserRepository;
import pl.polsl.photoplus.security.custom.CustomBasicAuthenticationFilter;
import pl.polsl.photoplus.security.custom.CustomLogoutHandler;
import pl.polsl.photoplus.security.custom.CustomUsernamePasswordAuthenticationFilter;
import pl.polsl.photoplus.security.services.TokenHoldingService;
import pl.polsl.photoplus.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
        extends WebSecurityConfigurerAdapter
{

    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final ModelPropertiesService modelPropertiesService;

    private final ObjectMapper objectMapper;

    private final TokenHoldingService tokenHoldingService;

    public SpringSecurityConfig(final UserDetailsServiceImpl userDetailsService, final UserRepository userRepository, final ModelPropertiesService modelPropertiesService, final ObjectMapper objectMapper, final TokenHoldingService tokenHoldingService)
    {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.modelPropertiesService = modelPropertiesService;
        this.objectMapper = objectMapper;
        this.tokenHoldingService = tokenHoldingService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new CustomUsernamePasswordAuthenticationFilter(objectMapper, modelPropertiesService, tokenHoldingService, authenticationManager()))
                .addFilter(new CustomBasicAuthenticationFilter(authenticationManager(), userRepository, modelPropertiesService, tokenHoldingService))
                .authorizeRequests()
                .antMatchers("/user/test")
                .permitAll()
                .antMatchers("/login")
                .permitAll()
                .antMatchers("/logout")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .addLogoutHandler(new CustomLogoutHandler())
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
    }

    @Bean
    PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

}
