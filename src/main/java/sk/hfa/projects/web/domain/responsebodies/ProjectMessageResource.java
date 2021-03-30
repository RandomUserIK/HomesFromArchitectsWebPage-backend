package sk.hfa.projects.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.projects.domain.Project;

@Data
@AllArgsConstructor
public class ProjectMessageResource implements MessageResource {

    private Project project;

}
