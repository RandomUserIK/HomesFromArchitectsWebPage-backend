package sk.hfa.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sk.hfa.auth.service.interfaces.IAuthenticationService;
import sk.hfa.configuration.security.filters.AuthorizationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HfaSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final IAuthenticationService authenticationService;

    @Value("${hfa.server.security.permit-patterns}")
    private String[] publicApiPatterns;

    public HfaSecurityConfiguration(AuthenticationEntryPoint authenticationEntryPoint,
                                    @Lazy IAuthenticationService authenticationService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationService = authenticationService;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); //NOSONAR
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

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
        http
                .cors()
                .and()
                .addFilterBefore(createAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers(publicApiPatterns).permitAll()
                .anyRequest().authenticated()
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
        http.csrf().ignoringAntMatchers("/api/auth/login", String.join(",", publicApiPatterns)) //NOSONAR
                   .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    private AuthorizationFilter createAuthorizationFilter() {
        return new AuthorizationFilter(authenticationService);
    }

}
