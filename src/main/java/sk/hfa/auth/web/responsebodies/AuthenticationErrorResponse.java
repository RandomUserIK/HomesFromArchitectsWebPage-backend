package sk.hfa.auth.web.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationErrorResponse implements MessageResource {

    private String title;

    private Integer status;

    private String message;

    private Long timestamp;

    public static AuthenticationErrorResponse build(String message) {
        return AuthenticationErrorResponse.builder()
                .message(message)
                .status(HttpStatus.UNAUTHORIZED.value())
                .title("Unauthorized")
                .timestamp(System.currentTimeMillis())
                .build();
    }

}
