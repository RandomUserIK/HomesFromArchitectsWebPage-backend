package sk.hfa.auth.web.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import sk.hfa.auth.domain.UserDetailsImpl;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse implements MessageResource {

    private Long id;

    private String username;

    private Set<String> roles;

    private Long jwtExpiration;

    private String jwtToken;

    public static AuthenticationResponse build(UserDetailsImpl userDetails, Long jwtExpiration, String jwtToken) {
        Set<String> userRoles = (Objects.isNull(userDetails.getAuthorities())) ? null :
                userDetails.getAuthorities().stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .collect(Collectors.toSet());

        return AuthenticationResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(userRoles)
                .jwtExpiration(jwtExpiration)
                .jwtToken(jwtToken)
                .build();
    }

}
