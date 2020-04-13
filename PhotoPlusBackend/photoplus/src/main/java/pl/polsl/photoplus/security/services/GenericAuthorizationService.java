package pl.polsl.photoplus.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.stereotype.Service;

@Service
public class GenericAuthorizationService
{

    public boolean hasAuthority(final String path, final String prefix){
        final String authority = prefix + path;
        return false;
    }
}
