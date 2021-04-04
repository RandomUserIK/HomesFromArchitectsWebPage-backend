package sk.hfa.projects.services.interfaces;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

import java.util.List;

public interface IProjectService {

    Project save(Project project);

    Project findById(Long id);

    Page<Project> getAllOnPage(int page);

    Page<Project> getAllOnPageAndCategory(int page, String category);

    Page<Project> getAll(Pageable pageable);

    Page<Project> getAllOnPageAndQuery(int page, Predicate predicate);

    Project build(ProjectRequest request);

}
