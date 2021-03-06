package sk.hfa.configuration.security.jwt.service.interfaces;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface IJwtService {

    String tokenFrom(Authentication authentication);

    boolean isValid(String token);

    String getSubjectFromToken(String token);

    Set<GrantedAuthority> getAuthoritiesFromToken(String token) throws ExpiredJwtException;

}
