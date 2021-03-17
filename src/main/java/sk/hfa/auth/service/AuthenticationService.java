package sk.hfa.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.User;
import sk.hfa.auth.service.interfaces.IAuthenticationService;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, IJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public User authenticateUser(String username, String password) {
        log.info("Authenticating user with the username: [" + username + "]");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User successfully authenticated. Generating new JWT token...");
        String token = jwtService.tokenFrom(authentication);

        // TODO
        return null;
    }

}
