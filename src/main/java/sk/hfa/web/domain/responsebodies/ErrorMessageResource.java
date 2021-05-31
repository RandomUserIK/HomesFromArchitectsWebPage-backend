package sk.hfa.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorMessageResource implements MessageResource {

    private Long timestamp;

    private Integer status;

    private String title;

    private String message;

    public static ErrorMessageResource build(String title, String message, Integer statusCode) {
        return ErrorMessageResource.builder()
                .title(title)
                .message(message)
                .status(statusCode)
                .timestamp(System.currentTimeMillis())
                .build();
    }

}
