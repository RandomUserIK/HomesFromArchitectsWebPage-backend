package sk.hfa.contact_form.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.projects.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class SubmittedContactFormResponse implements MessageResource {
    private String message;
}
