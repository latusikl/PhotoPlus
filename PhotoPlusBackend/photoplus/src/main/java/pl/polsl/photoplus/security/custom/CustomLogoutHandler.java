package pl.polsl.photoplus.security.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import pl.polsl.photoplus.components.ContextProvider;
import pl.polsl.photoplus.components.ModelPropertiesService;
import pl.polsl.photoplus.security.services.TokenHoldingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class CustomLogoutHandler
        implements LogoutHandler
{
    @Override
    public void logout(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Authentication authentication)
    {
        final ModelPropertiesService modelPropertiesService = ContextProvider.getBean(ModelPropertiesService.class);
        final Optional<String> token = getTokenWithPrefix(httpServletRequest, modelPropertiesService.getSecurityHeaderName());
        if (token.isPresent()) {
            final TokenHoldingService tokenHoldingService = ContextProvider.getBean(TokenHoldingService.class);
            final String tokenWithoutPrefix = token.get().replace(modelPropertiesService.getSecurityTokenPrefix(), "");
            if (tokenHoldingService != null && tokenHoldingService.isNotLoggedOut(tokenWithoutPrefix)) {
                tokenHoldingService.removeTokenOnLogout(tokenWithoutPrefix);
                log.info("Removed token on logout.");
                return;
            }
        }
        log.warn("Token was unable to remove while logging out.");
        return;
    }

    private Optional<String> getTokenWithPrefix(final HttpServletRequest httpServletRequest, final String headerName)
    {

        return Optional.of(httpServletRequest.getHeader(headerName));
    }
}
