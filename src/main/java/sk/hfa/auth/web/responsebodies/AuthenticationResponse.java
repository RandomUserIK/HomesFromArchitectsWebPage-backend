package sk.hfa.auth.web.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import sk.hfa.auth.domain.UserDetailsImpl;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Long id;

    private String username;

    private Set<String> roles;

    public static AuthenticationResponse build(UserDetailsImpl userDetails) {
        Set<String> userRoles = (Objects.isNull(userDetails.getAuthorities())) ? null :
                userDetails.getAuthorities().stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .collect(Collectors.toSet());

        return AuthenticationResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(userRoles)
                .build();
    }

}
