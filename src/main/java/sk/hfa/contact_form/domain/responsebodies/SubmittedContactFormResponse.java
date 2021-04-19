package sk.hfa.contact_form.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class SubmittedContactFormResponse implements MessageResource {
    private String message;
}
