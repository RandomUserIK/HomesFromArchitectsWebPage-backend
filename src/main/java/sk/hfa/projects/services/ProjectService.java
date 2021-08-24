package sk.hfa.projects.services;

import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sk.hfa.google.products.services.interfaces.IGoogleProductsService;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.InteriorDesignProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.repositories.ProjectRepository;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.InvalidProjectRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;
import sk.hfa.util.Constants;

import java.util.Objects;

@Service
public class ProjectService implements IProjectService {

    private static final String INVALID_CATEGORY_MESSAGE = "Invalid category provided";

    private final ProjectRepository projectRepository;
    private final IImageService imageService;
    private final IGoogleProductsService googleProductsService;

    public ProjectService(ProjectRepository projectRepository,
                          IGoogleProductsService googleProductsService,
                          @Lazy IImageService imageService) {
        this.projectRepository = projectRepository;
        this.imageService = imageService;
        this.googleProductsService = googleProductsService;
    }

    @Override
    public Project save(Project project) {
        project = projectRepository.save(project);
        String googleProductId = googleProductsService.createGoogleProduct(project);

        if (StringUtils.isBlank(project.getGoogleProductId())) {
            project.setGoogleProductId(googleProductId);
            project = projectRepository.save(project);
        }

        return project;
    }

    @Override
    public Project findById(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);

        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found on the given ID: [" + id + "]"));
    }

    @Override
    public void deleteById(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);

        Project project = findById(id);
        googleProductsService.removeGoogleProduct(project.getGoogleProductId());
        imageService.deleteProjectImages(id);
        projectRepository.deleteById(id);
    }

    @Override
    public Page<Project> getAllOnPage(int page, int size, Predicate predicate) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Project> result = projectRepository.findAll(predicate, pageRequest);

        if (page > result.getTotalPages())
            throw new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE);

        return result;
    }

    @Override
    public Page<Project> getAllOnPageAndQuery(int page, Predicate predicate) {
        PageRequest pageRequest = PageRequest.of(page, Constants.ELEMENTS_PER_PAGE);
        Page<Project> result = projectRepository.findAll(predicate, pageRequest);

        if (page > result.getTotalPages())
            throw new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE);

        return result;
    }

    @Override
    public Project build(ProjectRequest request) {
        if (Objects.isNull(request))
            throw new InvalidProjectRequestException("Invalid request body");

        if (!isValidCategory(request.getCategory()))
            throw new IllegalArgumentException(INVALID_CATEGORY_MESSAGE);

        Category projectCategory = getCategory(request.getCategory());
        if (Category.COMMON.equals(projectCategory))
            return CommonProject.build(request);
        else if (Category.INDIVIDUAL.equals(projectCategory))
            return IndividualProject.build(request);
        else
            return InteriorDesignProject.build(request);
    }

    private Category getCategory(String category) {
        if (Category.COMMON.name().equals(category))
            return Category.COMMON;
        else if (Category.INDIVIDUAL.name().equals(category))
            return Category.INDIVIDUAL;
        else if (Category.INTERIOR_DESIGN.name().equals(category))
            return Category.INTERIOR_DESIGN;

        return null;
    }

    private boolean isValidCategory(String category) {
        return !Objects.isNull(getCategory(category));
    }

}
