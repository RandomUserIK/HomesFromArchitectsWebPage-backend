package sk.hfa.auth.service.interfaces;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IAuthorizationService {

    Authentication authorizeUser(HttpServletRequest request);

}
