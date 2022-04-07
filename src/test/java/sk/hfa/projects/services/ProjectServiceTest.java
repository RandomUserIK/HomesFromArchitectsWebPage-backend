package sk.hfa.projects.services;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.images.domain.Image;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.QProject;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.repositories.ProjectRepository;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.services.providers.ProjectRequest_ProjectEntity;
import sk.hfa.projects.services.providers.ProjectServiceTestArgumentProvider;
import sk.hfa.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectServiceTest {

    @Autowired
    private IProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private IImageService imageService;

    @ParameterizedTest
    @ArgumentsSource(ProjectServiceTestArgumentProvider.class)
    void testSave_validRequestProject_shouldSave(ProjectRequest_ProjectEntity holder) {
        projectService.save(holder.getProjectRequest());

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(imageService, times(1)).save(any(MultipartFile.class));
        if (holder.getProject().getCategory().equals(Category.COMMON)) {
            verify(imageService, times(2)).save(any(List.class));
        } else {
            verify(imageService, times(1)).save(any(List.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ProjectServiceTestArgumentProvider.class)
    void testUpdate_validProjectRequest_shouldUpdate(ProjectRequest_ProjectEntity holder) {
        when(projectRepository.findById(holder.getProjectRequest().getId())).thenReturn(Optional.of(holder.getProject()));

        projectService.update(holder.getProjectRequest());

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(imageService, times(1)).deleteImage(any(Image.class));
        if (holder.getProject().getCategory().equals(Category.COMMON)) {
            verify(imageService, times(2)).deleteImages(any(List.class));
        } else {
            verify(imageService, times(1)).deleteImages(any(List.class));
        }
    }

    @Test
    void testFindById_withNullId_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> projectService.findById(null));
    }

    @Test
    void testFindById_withInvalidId_shouldThrowProjectNotFoundException() {
        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(1L));
    }

    @Test
    void testDeleteById_withInvalidId_shouldThrowProjectNotFoundException() {
        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteById(1L));
    }

    @ParameterizedTest
    @ArgumentsSource(ProjectServiceTestArgumentProvider.class)
    void testDeleteById_withValidId_shouldDeleteProject(ProjectRequest_ProjectEntity holder) {

        Long id = holder.getProjectRequest().getId();
        Optional<Project> project = Optional.of(holder.getProject());

        when(projectRepository.findById(id))
                .thenReturn(project);
        projectService.deleteById(id);

        verify(projectRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteImage(any(Image.class));
        if (holder.getProject().getCategory().equals(Category.COMMON)) {
            verify(imageService, times(2)).deleteImages(any(List.class));
        } else {
            verify(imageService, times(1)).deleteImages(any(List.class));
        }
    }

    @Test
    void testGetAllOnPage_withInvalidPage_shouldThrowInvalidPageableRequestException() {
        Predicate predicate = QProject.project.category.eq(Category.COMMON);
        Page<Project> page = new PageImpl<>(
                new ArrayList<>(),
                PageRequest.of(0, Constants.ELEMENTS_PER_PAGE),
                25);

        when(projectRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(page);
        assertThrows(
                InvalidPageableRequestException.class,
                () -> projectService.getAllOnPage(4, 25, predicate));
    }

    @Test
    void testGetAllOnPage_withValidRequest_shouldReturn() {
        Predicate predicate = QProject.project.category.eq(Category.COMMON);
        Page<Project> page = new PageImpl<>(
                new ArrayList<>(),
                PageRequest.of(0, Constants.ELEMENTS_PER_PAGE),
                25);

        when(projectRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(page);
        Page<Project> projects = projectService.getAllOnPage(0, Constants.ELEMENTS_PER_PAGE, predicate);
        assertNotNull(projects);
    }

    @Test
    void testGetAllOnPageAndQuery_withInvalidPage_shouldThrowInvalidPageableRequestException() {
        Predicate predicate = QProject.project.category.eq(Category.COMMON);
        Page<Project> page = new PageImpl<>(
                new ArrayList<>(),
                PageRequest.of(0, Constants.ELEMENTS_PER_PAGE),
                25);

        when(projectRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(page);
        assertThrows(
                InvalidPageableRequestException.class,
                () -> projectService.getAllOnPageAndQuery(4, predicate));
    }

    @Test
    void testGetAllOnPageAndQuery_withValidRequest_shouldReturn() {
        Predicate predicate = QProject.project.category.eq(Category.COMMON);
        Page<Project> page = new PageImpl<>(
                new ArrayList<>(),
                PageRequest.of(0, Constants.ELEMENTS_PER_PAGE),
                25);

        when(projectRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(page);
        Page<Project> projects = projectService.getAllOnPageAndQuery(0, predicate);
        assertNotNull(projects);
    }

}