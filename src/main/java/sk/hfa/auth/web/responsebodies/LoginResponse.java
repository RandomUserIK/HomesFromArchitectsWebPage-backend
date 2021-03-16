package sk.hfa.auth.web.responsebodies;

import lombok.Data;

import java.util.Set;

@Data
public class LoginResponse {

    private Long id;

    private String username;

    private Set<String> roles;

    private String token;

    private String type;

}
