package sk.hfa.auth.service.interfaces;

import sk.hfa.auth.web.responsebodies.AuthenticationResponse;

public interface IAuthenticationService {

    AuthenticationResponse authenticateUser(String username, String password);

}
