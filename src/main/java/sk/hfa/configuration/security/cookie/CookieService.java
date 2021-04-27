package sk.hfa.configuration.security.cookie;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import sk.hfa.configuration.security.cookie.interfaces.ICookieService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class CookieService implements ICookieService {

    private static final String COOKIE_BEARER = "COOKIE-BEARER";

    @Override
    public HttpCookie fromToken(String token) {
        if (Objects.isNull(token) || token.isEmpty())
            return null;

        // TODO: set secure attribute when https is configured
        return ResponseCookie.from(COOKIE_BEARER, token)
                .httpOnly(true)
                .path("/")
                .build();
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        if (Objects.isNull(request))
            return null;

        Cookie cookie = WebUtils.getCookie(request, COOKIE_BEARER);
        return Objects.isNull(cookie) ? null : cookie.getValue();
    }

}
