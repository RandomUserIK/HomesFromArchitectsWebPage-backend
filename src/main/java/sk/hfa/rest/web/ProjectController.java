package sk.hfa.rest.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.hfa.databases.projects.domains.IndividualProject;
import sk.hfa.databases.projects.services.interfaces.IIndividualProjectService;
import sk.hfa.rest.domain.throwable.InvalidPageableRequestException;
import sk.hfa.rest.responsebodies.IndividualProjectsMessageResource;
import sk.hfa.rest.responsebodies.MessageResource;

@RestController
@RequestMapping(path = "/api/project")
@CrossOrigin(origins =  "*", allowedHeaders = "*")
public class ProjectController {

    @Autowired
    private IIndividualProjectService individualProject;

    private static final int ELEMENTS_PER_PAGE = 9;

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
    public MessageResource getAllProjects(@RequestParam("page") int page) {
        PageRequest pageRequest = PageRequest.of(page, ELEMENTS_PER_PAGE);
        Page<IndividualProject> resultPage = individualProject.getAllIndividualProjects(pageRequest);
        if (page > resultPage.getTotalPages()) {
            throw new InvalidPageableRequestException("Invalid pageable request");
        }
        IndividualProjectsMessageResource messageResource = new IndividualProjectsMessageResource();
        messageResource.setIndividualProjects(resultPage.getContent());
        messageResource.setCurrentPage(page);
        messageResource.setTotalElements(resultPage.getTotalElements());
        messageResource.setElementsPerPage(ELEMENTS_PER_PAGE);

        return messageResource;
    }

    @GetMapping("keyword")
    public MessageResource getProductsByKeyword(@RequestParam("page") int page, @RequestParam("keyword") String keyword) {
        PageRequest pageRequest = PageRequest.of(page, ELEMENTS_PER_PAGE);
        Page<IndividualProject> resultPage = individualProject.getAllBySearchKeyword(pageRequest, keyword);
        if (page > resultPage.getTotalPages()) {
            throw new InvalidPageableRequestException("Invalid pageable request");
        }

        IndividualProjectsMessageResource messageResource = new IndividualProjectsMessageResource();
        messageResource.setIndividualProjects(resultPage.getContent());
        messageResource.setCurrentPage(page);
        messageResource.setTotalElements(resultPage.getTotalElements());
        messageResource.setElementsPerPage(ELEMENTS_PER_PAGE);

        return messageResource;
    }


}
