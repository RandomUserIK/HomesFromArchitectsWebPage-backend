package sk.hfa.configuration.security.jwt.service.interfaces;

import java.util.Map;

public interface IJwtService {

    String tokenFrom(Map<String, Object> claims);

    boolean isExpired(String token);

    boolean isValid(String token);

}
