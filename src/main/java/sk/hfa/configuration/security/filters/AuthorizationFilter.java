package sk.hfa.configuration.security.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.hfa.auth.domain.throwable.InvalidJwtTokenException;
import sk.hfa.auth.service.interfaces.IAuthorizationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String UNAUTHORIZED = "Unauthorized";

    private final IAuthorizationService authorizationService;

    public AuthorizationFilter(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Authorizing access to " + request.getMethod() + " " + request.getRequestURI());
            Authentication authentication = authorizationService.authorizeUser(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User [" + authentication.getPrincipal() + "] authorized.");
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                IllegalArgumentException | InvalidJwtTokenException ex) {
            log.error(UNAUTHORIZED, ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);
        }
    }

}
