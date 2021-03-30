package sk.hfa.projects.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;

import java.util.List;

public interface IProjectService {

    Project createNewProject(String title, Category category);

    Project save(Project project);

    Project findById(Long id);

    Project findByTitle(String title);

    Page<Project> getAllOnPage(int page);

    Page<Project> getAllOnPageAndKeyword(int page, String keyword);

    Page<Project> getAll(Pageable pageable);

    Page<Project> findAllByKeyword(Pageable pageable, String keyword);

    List<Project> saveAll(List<Project> projects);

}
