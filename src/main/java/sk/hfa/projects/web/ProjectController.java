package sk.hfa.projects.web;

import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.IndividualProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.InteriorProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;
import sk.hfa.projects.web.domain.responsebodies.ProjectMessageResource;
import sk.hfa.projects.web.domain.responsebodies.ProjectPageMessageResource;
import sk.hfa.web.domain.responsebodies.DeleteEntityMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/api/projects")
public class ProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/individual", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> createIndividualProject(@Valid IndividualProjectRequest request) {
        return createProject(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/common", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> createCommonProjectRequest(@Valid CommonProjectRequest request) {
        return createProject(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/interior_design", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> createInteriorProjectRequest(@Valid InteriorProjectRequest request) {
        return createProject(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/individual", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> updateIndividualProject(@Valid IndividualProjectRequest request) {
        return updateProject(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/common", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> updateCommonProjectRequest(@Valid CommonProjectRequest request) {
        return updateProject(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/interior_design", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResource> updateInteriorProjectRequest(@Valid InteriorProjectRequest request) {
        return updateProject(request);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getProject(@PathVariable long id) {
        log.info("Fetching the project with the ID: " + id);
        Project project = projectService.findById(id);
        MessageResource responseBody = new ProjectMessageResource(project);
        return ResponseEntity.ok(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> deleteProject(@PathVariable long id) {
        log.info("Deleting the project with the ID: [" + id + "]");
        projectService.deleteById(id);
        MessageResource responseBody = new DeleteEntityMessageResource("Project successfully deleted");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getAllOnPage(@RequestParam("page") int page,
                                                        @RequestParam("size") int size,
                                                        @QuerydslPredicate(root = Project.class) Predicate predicate) {
        log.info("Fetching projects on the page [" + page + "]");
        Page<Project> result = projectService.getAllOnPage(page, size, predicate);
        MessageResource responseBody = ProjectPageMessageResource.build(result);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getAllOnPageAndQuery(@RequestParam("page") int page,
                                                                @QuerydslPredicate(root = Project.class) Predicate predicate) {
        log.info("Fetching projects on the page [" + page + "] and filtering on custom query [" + predicate.toString() + "].");
        Page<Project> result = projectService.getAllOnPageAndQuery(page, predicate);
        MessageResource responseBody = ProjectPageMessageResource.build(result);
        return ResponseEntity.ok(responseBody);
    }

    private ResponseEntity<MessageResource> createProject(ProjectRequest request) {
        log.info("Creating a new project.");
        Project project = projectService.save(request);
        MessageResource responseBody = new ProjectMessageResource(project);
        log.info("The project with the ID: [" + project.getId() + "] was successfully created.");
        return ResponseEntity.ok(responseBody);
    }

    private ResponseEntity<MessageResource> updateProject(ProjectRequest request) {
        log.info("Creating a new project.");
        Project project = projectService.update(request);
        MessageResource responseBody = new ProjectMessageResource(project);
        log.info("The project with the ID: [" + project.getId() + "] was successfully created.");
        return ResponseEntity.ok(responseBody);
    }

}
