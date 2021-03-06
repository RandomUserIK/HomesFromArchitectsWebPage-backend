package sk.hfa.projects.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import sk.hfa.projects.domain.Project;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.MessageResource;

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
            throw new IllegalArgumentException(Constants.INVALID_PAGE_MESSAGE);

        return ProjectPageMessageResource.builder()
                .currentPage(page.getNumber())
                .projects(page.getContent())
                .totalElements(page.getTotalElements())
                .elementsPerPage(Constants.ELEMENTS_PER_PAGE)
                .build();
    }

}
