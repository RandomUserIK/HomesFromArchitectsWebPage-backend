package sk.hfa.auth.service.interfaces;

import org.springframework.security.core.Authentication;
import sk.hfa.auth.web.requestbodies.AuthenticationRequest;
import sk.hfa.web.domain.responsebodies.MessageResource;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

    MessageResource authenticateUser(AuthenticationRequest authenticationRequest);

    Authentication authorizeUser(HttpServletRequest request);

}
