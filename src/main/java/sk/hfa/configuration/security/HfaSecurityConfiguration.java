package sk.hfa.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HfaSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final List<String> EXPOSED_HEADERS = Arrays.asList(
            "x-auth-token", "Access-Control-Allow-Credentials", "Access-Control-Allow-Origin", "Cache-Control",
            "Content-Type", "Expires", "Pragma", "X-XSS-Protection", "X-Content-Type-Options", "X-Frame-Options"
    );

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final IJwtService jwtService;
    private final UserDetailsService userDetailsServiceImpl;

    @Value("${hfa.server.security.permit-patterns}")
    private String[] publicApiPatterns;

    @Value("${hfa.server.security.csrf}")
    private boolean isCsrfEnabled;

    public HfaSecurityConfiguration(AuthenticationEntryPoint authenticationEntryPoint, IJwtService jwtService,
                                    UserDetailsService userDetailsServiceImpl) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        EXPOSED_HEADERS.stream().forEach(configuration::addExposedHeader);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: configure CORS policy
        http
                .cors().disable()
                .addFilterBefore(createAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                        .antMatchers(publicApiPatterns).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                    .logoutUrl("/api/auth/logout")
                    .invalidateHttpSession(true)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .headers()
                    .frameOptions().sameOrigin();

        setCsrf(http);
    }

    private void setCsrf(HttpSecurity http) throws Exception {
        if (isCsrfEnabled) {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        } else {
            http.csrf().disable();
        }
    }

    private AuthenticationFilter createAuthenticationFilter() {
        return new AuthenticationFilter(jwtService, userDetailsServiceImpl);
    }

}
