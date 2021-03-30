package sk.hfa.projects.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.services.ProjectService;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class ProjectPageMessageResource implements MessageResource {

    private int currentPage;

    private long totalElements;

    private int elementsPerPage;

    private List<Project> projects;

    public static ProjectPageMessageResource build(Page<Project> page) {
        if (Objects.isNull(page))
            throw new IllegalArgumentException("Invalid page provided");

        return ProjectPageMessageResource.builder()
                .projects(page.getContent())
                .totalElements(page.getTotalElements())
                .elementsPerPage(ProjectService.ELEMENTS_PER_PAGE)
                .build();
    }

}
