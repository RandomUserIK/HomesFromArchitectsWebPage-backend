package sk.hfa.projects.services.interfaces;

import com.querydsl.core.types.Predicate;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

public interface IProjectService {

    Project save(@NonNull ProjectRequest project);

    Project update(@NonNull ProjectRequest request);

    Project findById(Long id);

    void deleteById(Long id);

    Page<Project> getAllOnPage(int page, int size, Predicate predicate);

    Page<Project> getAllOnPageAndQuery(int page, Predicate predicate);

}
