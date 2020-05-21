package pl.polsl.photoplus.security.custom;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import pl.polsl.photoplus.components.ModelPropertiesService;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.security.services.TokenHoldingService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class CustomUsernamePasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter
{
    private final ObjectMapper objectMapper;

    private final ModelPropertiesService modelPropertiesService;

    private final TokenHoldingService tokenHoldingService;

    private final AuthenticationManager authenticationManager;

    public CustomUsernamePasswordAuthenticationFilter(final ObjectMapper objectMapper, final ModelPropertiesService modelPropertiesService, final TokenHoldingService tokenHoldingService, final AuthenticationManager authenticationManager)
    {
        this.objectMapper = objectMapper;
        this.modelPropertiesService = modelPropertiesService;
        this.tokenHoldingService = tokenHoldingService;
        this.authenticationManager = authenticationManager;
    }

    @SneakyThrows(IOException.class)
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws
                                                                                                                      AuthenticationException
    {
        final Map<String,String> credentials;
        try {
            credentials = readCredentials(request.getInputStream());
        } catch (final IOException e) {
            throw new PreAuthenticatedCredentialsNotFoundException("Unable to read credentials from request body.");
        }
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials
                                                                                                                        .get("login"), credentials
                                                                                                                        .get("password"));
        try {
            final Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (final BadCredentialsException | DisabledException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println("{ \"error\": \"" + e.getMessage() + ".\" }");
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws
                                                                                                                                                                            IOException
    {
        final User user = (User)authResult.getPrincipal();
        final Pair<String,Date> tokenPair = createTokenAndAddToTokenHolder(user.getLogin());
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader(modelPropertiesService.securityHeaderName, modelPropertiesService.securityTokenPrefix + tokenPair
                .getFirst());
        response.addHeader("Expires", tokenPair.getSecond().toString());
        addUserCredentials(user, response);
    }

    private void addUserCredentials(final User user, final HttpServletResponse response) throws IOException
    {
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write(getUserJson(user));
    }

    private String getUserJson(final User user)
    {
        final ObjectNode userNode = objectMapper.createObjectNode();
        userNode.put("login", user.getLogin());
        userNode.put("code", user.getCode());
        userNode.put("role", user.getUserRole().getValue());
        return userNode.toPrettyString();
    }

    private Map<String,String> readCredentials(final InputStream inputStream) throws IOException
    {
        final JsonNode jsonNode = objectMapper.readTree(inputStream);
        final String login = jsonNode.get("login").asText();
        final String password = jsonNode.get("password").asText();
        return Map.of("login", login, "password", password);
    }

    private Pair<String,Date> createTokenAndAddToTokenHolder(final String login)
    {
        final Date expirationDate = new Date(System.currentTimeMillis() + modelPropertiesService.securityExpiration);
        final String token = JWT.create()
                .withSubject(login)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(modelPropertiesService.securitySecret.getBytes()));
        addTokenToTokenHolder(token, expirationDate);
        return Pair.of(token, expirationDate);
    }

    private void addTokenToTokenHolder(final String token, final Date expirationDate)
    {
        tokenHoldingService.addTokenOnLogin(token, expirationDate);
    }

}
