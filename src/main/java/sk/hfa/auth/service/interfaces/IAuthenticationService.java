package sk.hfa.auth.service.interfaces;

import sk.hfa.auth.domain.User;

public interface IAuthenticationService {

    User authenticateUser(String username, String password);

}
