package sk.hfa.auth.service;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.throwable.InvalidJwtTokenException;
import sk.hfa.auth.service.interfaces.IAuthorizationService;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;
import sk.hfa.util.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

@Service
public class AuthorizationService implements IAuthorizationService {

    private final IJwtService jwtService;

    public AuthorizationService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authorizeUser(HttpServletRequest request) {
        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(jwtHeader) || !jwtHeader.startsWith(Constants.TOKEN_TYPE))
            throw new IllegalArgumentException("JWT Token not found");

        String token = jwtHeader.replace(Constants.TOKEN_TYPE, "");
        if (!jwtService.isValid(token))
            throw new InvalidJwtTokenException("Invalid JWT Token provided.");

        String username = jwtService.getSubjectFromToken(token);
        if (Objects.isNull(username) || username.isEmpty())
            throw new IllegalArgumentException("Failed to extract username from the provided JWT token");

        Set<GrantedAuthority> authorities = jwtService.getAuthoritiesFromToken(token);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}
