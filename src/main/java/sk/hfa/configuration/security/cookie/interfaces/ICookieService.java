package sk.hfa.configuration.security.cookie.interfaces;

import org.springframework.http.HttpCookie;

import javax.servlet.http.HttpServletRequest;

public interface ICookieService {

    HttpCookie fromToken(String token);

    String extractToken(HttpServletRequest request);

}
