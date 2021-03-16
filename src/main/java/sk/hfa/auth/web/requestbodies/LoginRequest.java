package sk.hfa.auth.web.requestbodies;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;

}
