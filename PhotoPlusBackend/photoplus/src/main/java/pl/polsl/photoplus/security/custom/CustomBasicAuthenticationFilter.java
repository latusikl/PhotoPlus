package pl.polsl.photoplus.security.custom;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.polsl.photoplus.components.ModelPropertiesService;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.UserRepository;
import pl.polsl.photoplus.security.services.TokenHoldingService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class CustomBasicAuthenticationFilter
        extends BasicAuthenticationFilter
{
    private final UserRepository userRepository;

    private final ModelPropertiesService modelPropertiesService;

    private final TokenHoldingService tokenHoldingService;

    public CustomBasicAuthenticationFilter(final AuthenticationManager authenticationManager, final UserRepository userRepository, final ModelPropertiesService modelPropertiesService, final TokenHoldingService tokenHoldingService)
    {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.modelPropertiesService = modelPropertiesService;
        this.tokenHoldingService = tokenHoldingService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws IOException, ServletException
    {
        final String header = request.getHeader(modelPropertiesService.securityHeaderName);

        if (header == null || !header.startsWith(modelPropertiesService.securityTokenPrefix)) {
            chain.doFilter(request, response);
            return;
        }
        final Authentication authentication = getLoginPasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication getLoginPasswordAuthentication(final HttpServletRequest request)
    {
        final String token = request.getHeader(modelPropertiesService.securityHeaderName);
        final String tokenWithoutPrefix = token.replace(modelPropertiesService.securityTokenPrefix, "");
        if (token != null)
        {
            try {
                final DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(modelPropertiesService.securitySecret.getBytes()))
                        .build()
                        .verify(tokenWithoutPrefix);
                final String login = decodedJwt.getSubject();
                if (login != null && isNotLoggedOut(tokenWithoutPrefix)) {
                    final Optional<User> foundUser = userRepository.findUserByLogin(login);
                    if (foundUser.isPresent()) {
                        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(login, null, foundUser
                                .get()
                                .getAuthorities());
                        return auth;
                    }

                }

            } catch (final TokenExpiredException e) {
                log.info("Token expired.");
            }
        }
        
        return null;
    }

    private boolean isNotLoggedOut(final String token)
    {
        return tokenHoldingService.isNotLoggedOut(token);
    }

}
