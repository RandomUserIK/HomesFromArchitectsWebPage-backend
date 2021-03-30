package sk.hfa.rest.responsebodies;

import lombok.Getter;
import lombok.Setter;
import sk.hfa.databases.projects.domains.IndividualProject;

import java.util.List;

@Getter
@Setter
public class IndividualProjectsMessageResource extends MessageResource {

    private List<IndividualProject> individualProjects;
    private int currentPage;
    private long totalElements;
    private int elementsPerPage;

}
