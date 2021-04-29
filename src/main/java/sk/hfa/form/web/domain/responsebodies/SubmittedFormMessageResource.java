package sk.hfa.form.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class SubmittedFormMessageResource implements MessageResource {
    private String message;
}
