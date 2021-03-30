package sk.hfa.projects.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.repositories.ProjectRepository;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService implements IProjectService {

    public static final int ELEMENTS_PER_PAGE = 9;

    private static final String INVALID_PAGEABLE_MESSAGE = "Invalid pageable request";

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createNewProject(String title, Category category) {
        Project newProject = (category.compareTo(Category.COMMON) == 0) ? new CommonProject() : new IndividualProject();
        newProject.setCategory(category);
        newProject.setTitle(title);
        return newProject;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project findById(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException("Invalid identifier provided");

        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found on the given ID: [" + id.toString() + "]"));
    }

    @Override
    public Project findByTitle(String title) {
        if (Objects.isNull(title) || title.isEmpty())
            throw new IllegalArgumentException("Invalid title provided");

        return projectRepository.findByTitle(title).orElseThrow(() ->
                new ProjectNotFoundException("Project not found on the given title: [" + title + "]"));
    }

    @Override
    public Page<Project> getAllOnPage(int page) {
        PageRequest pageRequest = PageRequest.of(page, ELEMENTS_PER_PAGE);
        Page<Project> result = getAll(pageRequest);

        if (page > result.getTotalPages())
            throw new InvalidPageableRequestException(INVALID_PAGEABLE_MESSAGE);

        return result;
    }

    @Override
    public Page<Project> getAllOnPageAndKeyword(int page, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, ELEMENTS_PER_PAGE);
        Page<Project> result = findAllByKeyword(pageRequest, keyword);

        if (page > result.getTotalPages())
            throw new InvalidPageableRequestException(INVALID_PAGEABLE_MESSAGE);

        return result;
    }

    @Override
    public Page<Project> getAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Page<Project> findAllByKeyword(Pageable pageable, String keyword) {
        return projectRepository.findByTitleStartsWith(keyword, pageable);
    }

    @Override
    public List<Project> saveAll(List<Project> projects) {
        return projectRepository.saveAll(projects);
    }

}
