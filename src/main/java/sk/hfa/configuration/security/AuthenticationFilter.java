package sk.hfa.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    public AuthenticationFilter(IJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // if (isAdminApi(request.getRequestURI())) {
        //
        // }

        filterChain.doFilter(request, response);
    }

    // private boolean isAdminApi(String path) {
    //     return false;
    // }

}
