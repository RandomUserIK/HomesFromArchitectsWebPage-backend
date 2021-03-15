package sk.hfa.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final ProviderManager providerManager;

    @Autowired
    public AuthenticationFilter(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAdminApi(request.getRequestURI())) {

        }

        filterChain.doFilter(request, response);
    }

    private boolean isAdminApi(String path) {
        return false;
    }

}
