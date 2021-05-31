package sk.hfa.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteEntityMessageResource implements MessageResource {

    private String message;

}
