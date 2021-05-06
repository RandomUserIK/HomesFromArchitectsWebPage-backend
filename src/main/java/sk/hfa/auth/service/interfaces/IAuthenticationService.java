package sk.hfa.auth.service.interfaces;

import sk.hfa.auth.web.requestbodies.AuthenticationRequest;
import sk.hfa.web.domain.responsebodies.MessageResource;

public interface IAuthenticationService {

    MessageResource authenticateUser(AuthenticationRequest authenticationRequest);

}
