package sk.hfa.auth.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import sk.hfa.auth.web.requestbodies.AuthenticationRequest;
import sk.hfa.auth.web.responsebodies.AuthenticationResponse;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

    ResponseEntity<AuthenticationResponse> authenticateUser(AuthenticationRequest authenticationRequest);

    Authentication authorizeUser(HttpServletRequest request);

}
