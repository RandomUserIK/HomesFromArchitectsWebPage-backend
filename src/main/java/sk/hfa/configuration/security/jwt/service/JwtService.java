package sk.hfa.configuration.security.jwt.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.UserDetailsImpl;
import sk.hfa.configuration.security.jwt.service.interfaces.IJwtService;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService implements IJwtService {

    private static final String AUTHORITIES_CLAIM = "authorities";

    @Value("${hfa.server.security.jwt.secret}")
    private String secret;

    @Value("${hfa.server.security.jwt.expiration}")
    private long expiration;

    @Override
    public String tokenFrom(Authentication authentication) {
        log.info("Creating a new JWT token.");
        return Jwts.builder()
                .setSubject(((UserDetailsImpl) authentication.getPrincipal()).getUsername())
                .claim(AUTHORITIES_CLAIM, getAuthorities(authentication))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.warn("Provided JWT token is invalid.", ex);
            return false;
        }
    }

    @Override
    public String getSubjectFromToken(String token)  throws ExpiredJwtException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Set<GrantedAuthority> getAuthoritiesFromToken(String token) throws ExpiredJwtException {
        return Arrays.stream(getAllClaimsFromToken(token).get(AUTHORITIES_CLAIM).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private String getAuthorities(Authentication authentication) {
        return authentication == null ? "" : authentication.getAuthorities().stream()
                                                                            .map(GrantedAuthority::getAuthority)
                                                                            .collect(Collectors.joining(","));
    }

}
