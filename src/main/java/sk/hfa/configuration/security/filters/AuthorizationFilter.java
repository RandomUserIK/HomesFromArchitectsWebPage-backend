package sk.hfa.configuration.security.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.hfa.auth.service.interfaces.IAuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String UNAUTHORIZED = "Unauthorized";

    private final IAuthenticationService authenticationService;

    private final String[] publicApiPatterns;

    public AuthorizationFilter(IAuthenticationService authenticationService, String[] publicApiPatterns) {
        this.authenticationService = authenticationService;
        this.publicApiPatterns = publicApiPatterns;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if (Arrays.stream(publicApiPatterns).anyMatch(pattern -> request.getRequestURI().contains(pattern))) {
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authentication = authenticationService.authorizeUser(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException ex) {
            log.error(UNAUTHORIZED, ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);
        }
    }

}
