package sk.hfa.configuration.security.jwt.service.interfaces;

import org.springframework.security.core.Authentication;

public interface IJwtService {

    String tokenFrom(Authentication authentication);

    boolean isExpired(String token);

    boolean isValid(String token);

    String getSubjectFromToken(String token);

}
