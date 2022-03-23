package sk.hfa.projects.services;

import com.querydsl.core.types.Predicate;
import lombok.NonNull;
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

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

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
    public Project save(@NonNull ProjectRequest request) {
        Category projectCategory = ProjectUtils.getCategory(request.getCategory());
        Image titleImage = imageService.save(request.getTitleImageFile());
        List<Image> galleryImages = imageService.save(request.getGalleryImageFiles());
        Project project;

        if (Category.COMMON.equals(projectCategory)) {
            List<Image> floorPlanImages = imageService.save(((CommonProjectRequest) request).getFloorPlanImageFiles());
            project = CommonProject.build((CommonProjectRequest) request, titleImage, galleryImages, floorPlanImages);
        } else if (Category.INDIVIDUAL.equals(projectCategory)) {
            project = IndividualProject.build((IndividualProjectRequest) request, titleImage, galleryImages);
        } else {
            project = InteriorDesignProject.build((InteriorProjectRequest) request, titleImage, galleryImages);
        }
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project update(@NonNull ProjectRequest request) {
        Project oldProject = findById(request.getId());
        deleteImages(oldProject);
        return save(request);
    }

    @Override
    public Project findById(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);
        }
        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found on the given ID: [" + id + "]"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found on the given ID: [" + id + "]"));
        deleteImages(project);
        projectRepository.deleteById(id);
    }

    @Override
    public Page<Project> getAllOnPage(int page, int size, Predicate predicate) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Project> result = projectRepository.findAll(predicate, pageRequest);

        if (page > result.getTotalPages()) {
            throw new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE);
        }
        return result;
    }

    @Override
    public Page<Project> getAllOnPageAndQuery(int page, Predicate predicate) {
        PageRequest pageRequest = PageRequest.of(page, Constants.ELEMENTS_PER_PAGE);
        Page<Project> result = projectRepository.findAll(predicate, pageRequest);

        if (page > result.getTotalPages()) {
            throw new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE);
        }
        return result;
    }

    private void deleteImages(@NotNull Project project) {
        imageService.deleteImage(project.getTitleImage());
        imageService.deleteImages(project.getGalleryImages());
        if (Category.COMMON.equals(project.getCategory())) {
            imageService.deleteImages(((CommonProject) Hibernate.unproxy(project)).getFloorPlanImages());
        }
    }

}
