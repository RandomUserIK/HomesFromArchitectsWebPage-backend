package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.services.interfaces.IProjectService;
import sk.hfa.projects.web.domain.responsebodies.MessageResource;
import sk.hfa.projects.web.domain.responsebodies.ProjectMessageResource;
import sk.hfa.projects.web.domain.responsebodies.ProjectPageMessageResource;

@Slf4j
@RestController
@RequestMapping(path = "/api/projects/")
public class ProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    // TODO:
    // @PreAuthorize("hasRole('ADMIN')")
    // @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<MessageResource> addProject(@RequestBody ProjectRequestBody project) {
    //     return null;
    // }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getProject(@PathVariable long id) {
        Project project = projectService.findById(id);
        MessageResource responseBody = new ProjectMessageResource(project);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = "{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getAll(@PathVariable int page) {
        Page<Project> result = projectService.getAllOnPage(page);
        MessageResource responseBody = ProjectPageMessageResource.build(result);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = "{page}/{keyword}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getAllOnKeyword(@PathVariable int page, @PathVariable String keyword) {
        Page<Project> result = projectService.getAllOnPageAndKeyword(page, keyword);
        MessageResource responseBody = ProjectPageMessageResource.build(result);
        return ResponseEntity.ok(responseBody);
    }

}
