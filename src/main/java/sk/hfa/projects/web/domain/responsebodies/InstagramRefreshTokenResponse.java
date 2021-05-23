package sk.hfa.projects.web.domain.responsebodies;

import lombok.Data;

@Data
public class InstagramRefreshTokenResponse {

    private String access_token;
    private String token_type;
    private int expires_in;

}
