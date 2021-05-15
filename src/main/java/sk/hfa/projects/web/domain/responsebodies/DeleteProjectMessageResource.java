package sk.hfa.projects.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class DeleteProjectMessageResource implements MessageResource {

    private String message;

}
