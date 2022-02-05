package sk.hfa.projects.services;

import com.querydsl.core.types.Predicate;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.hfa.images.domain.Image;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.InteriorDesignProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.repositories.ProjectRepository;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.util.ProjectUtils;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.IndividualProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.InteriorProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;
import sk.hfa.util.Constants;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {


    private final ProjectRepository projectRepository;
    private final IImageService imageService;

    public ProjectService(ProjectRepository projectRepository,
                          @Lazy IImageService imageService) {
        this.projectRepository = projectRepository;
        this.imageService = imageService;
    }

    /**
     * Persist project request and also persist respective images and upload them to files system.
     *
     * @param request Project request.
     * @return Persisted project
     */
    @Override
    @Transactional
    public Project save(ProjectRequest request) {
        ProjectUtils.validateProjectRequest(request);
        Category projectCategory = ProjectUtils.validateAndGetCategory(request);
        Project project = build(request, projectCategory);
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project update(ProjectRequest request) {
        ProjectUtils.validateProjectRequest(request);
        Category projectCategory = ProjectUtils.validateAndGetCategory(request);
        Project oldProject = findById(request.getId());
        deleteImages(oldProject);
        return projectRepository.save(build(request, projectCategory));
    }

    @Override
    public Project findById(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);

        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found on the given ID: [" + id + "]"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if (!project.isPresent())
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);

        deleteImages(project.get());
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

    private Project build(ProjectRequest request, Category projectCategory) {
        Image titleImage = imageService.save(request.getTitleImageFile());
        List<Image> galleryImages = imageService.save(request.getGalleryImageFiles());

        if (Category.COMMON.equals(projectCategory)) {
            List<Image> floorPlanImages = imageService.save(((CommonProjectRequest) request).getFloorPlanImageFiles());
            return CommonProject.build((CommonProjectRequest) request, titleImage, galleryImages, floorPlanImages);
        } else if (Category.INDIVIDUAL.equals(projectCategory)) {
            return IndividualProject.build((IndividualProjectRequest) request, titleImage, galleryImages);
        } else {
            return InteriorDesignProject.build((InteriorProjectRequest) request, titleImage, galleryImages);
        }
    }

    private void deleteImages(Project project) {
        imageService.deleteImage(project.getTitleImage());
        imageService.deleteImages(project.getGalleryImages());
        if (Category.COMMON.equals(project.getCategory())) {
            imageService.deleteImages(((CommonProject) Hibernate.unproxy(project)).getFloorPlanImages());
        }
    }

}
