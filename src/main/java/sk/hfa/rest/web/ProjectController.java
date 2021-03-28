package sk.hfa.rest.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.hfa.databases.projects.domains.IndividualProject;
import sk.hfa.databases.projects.services.interfaces.IIndividualProjectService;
import sk.hfa.rest.domain.throwable.InvalidPageableRequestException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/project")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {

    @Autowired
    private IIndividualProjectService individualProject;

    @PostMapping(path = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    String addProject(@RequestBody IndividualProject entity) {
        IndividualProject project = individualProject.findIndividualProjectById(Long.valueOf(entity.getId().toString()));
        if (project != null)
            return "Project already exist";
        individualProject.save(entity);
        return "Project Created";
    }

    @PostMapping(path = "/concrete")
    IndividualProject getProject(@RequestParam("projectId") String projectId) {
        return individualProject.findIndividualProjectById(Long.valueOf(projectId));
    }

    @GetMapping("all")
    public List<IndividualProject> getAllProjects(@RequestParam("page") int page) throws Exception {
        PageRequest pageRequest = PageRequest.of(page, 9);
        Page<IndividualProject> resultPage = individualProject.getAllIndividualProjects(pageRequest);
        if (page > resultPage.getTotalPages()) {
            throw new InvalidPageableRequestException("Invalid pageable request");
        }
        return resultPage.getContent();
    }
}
