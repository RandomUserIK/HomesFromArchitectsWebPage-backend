package sk.hfa.rest.responsebodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidPageRequestMessageResource extends MessageResource {

    private String code;
    private String message;
    private long timestamp;

}
