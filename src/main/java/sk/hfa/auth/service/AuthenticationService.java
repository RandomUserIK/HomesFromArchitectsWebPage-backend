package sk.hfa.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.UserDetailsImpl;
import sk.hfa.auth.service.interfaces.IAuthenticationService;
import sk.hfa.auth.web.requestbodies.AuthenticationRequest;
import sk.hfa.auth.web.responsebodies.AuthenticationResponse;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    private static final String TOKEN_TYPE = "Bearer ";

    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    @Value("${hfa.server.security.jwt.expiration}")
    private Long jwtExpiration;

    public AuthenticationService(AuthenticationManager authenticationManager, IJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public MessageResource authenticateUser(AuthenticationRequest authenticationRequest) {
        log.info("Authenticating user with the username: [" + authenticationRequest.getUsername() + "]");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                                        authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User [" + authenticationRequest.getUsername() + "] successfully authenticated.");
        String token = jwtService.tokenFrom(authentication);
        return AuthenticationResponse.build((UserDetailsImpl) authentication.getPrincipal(), jwtExpiration, TOKEN_TYPE + token);
    }

}
