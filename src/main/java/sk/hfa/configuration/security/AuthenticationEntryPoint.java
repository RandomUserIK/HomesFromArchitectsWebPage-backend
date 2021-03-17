package sk.hfa.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Value("${hfa.server.security.realm-name}")
    private String realmName;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException != null) {
            log.warn("Unauthorized", authException);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("HTTP Status 401 - " + authException.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName(realmName);
        super.afterPropertiesSet();
    }

}
