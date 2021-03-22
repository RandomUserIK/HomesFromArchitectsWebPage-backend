package sk.hfa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk.hfa.databases.projects.domains.IndividualProject;
import sk.hfa.databases.projects.services.interfaces.IIndividualProjectService;

@Controller
@RequestMapping(path = "/project")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {

    @Autowired
    private IIndividualProjectService individualProject;

    @PostMapping(path = "/add")
    @ResponseBody String addProject(@RequestBody IndividualProject entity) {
       IndividualProject project = individualProject.findIndividualProjectById( Long.valueOf(entity.getId().toString()));
        if(project != null)
            return "Project already exist";
        individualProject.save(entity);
        return "Project Created";
    }

    @PostMapping(path = "/concrete")
    @ResponseBody IndividualProject getProject(@RequestParam("projectId") String projectId) {
        return individualProject.findIndividualProjectById(Long.valueOf(projectId));
    }

    @GetMapping(path = "/all")
    @ResponseBody Iterable<IndividualProject> getAllProjects() {
        return individualProject.getAllIndividualProjects();
    }
}
