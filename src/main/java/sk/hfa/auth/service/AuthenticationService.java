package sk.hfa.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.UserDetailsImpl;
import sk.hfa.auth.service.interfaces.IAuthenticationService;
import sk.hfa.auth.web.requestbodies.AuthenticationRequest;
import sk.hfa.auth.web.responsebodies.AuthenticationResponse;
import sk.hfa.configuration.security.cookie.interfaces.ICookieService;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final ICookieService cookieService;
    private final IJwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, ICookieService cookieService,
                                 IJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateUser(AuthenticationRequest authenticationRequest) {
        log.info("Authenticating user with the username: [" + authenticationRequest.getUsername() + "]");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                                        authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User [" + authenticationRequest.getUsername() + "] successfully authenticated.");
        AuthenticationResponse response = AuthenticationResponse.build((UserDetailsImpl) authentication.getPrincipal());
        String token = jwtService.tokenFrom(authentication);
        HttpCookie cookie = cookieService.fromToken(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @Override
    public Authentication authorizeUser(HttpServletRequest request) {
        String token = cookieService.extractToken(request);

        if (Objects.isNull(token) || token.isEmpty())
            throw new IllegalArgumentException("JWT Token not found");

        String username = jwtService.getSubjectFromToken(token);

        if (Objects.isNull(username) || username.isEmpty())
            throw new IllegalArgumentException("Failed to extract username from the provided JWT token");

        Collection<? extends GrantedAuthority> authorities = jwtService.getAuthoritiesFromToken(token);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}
