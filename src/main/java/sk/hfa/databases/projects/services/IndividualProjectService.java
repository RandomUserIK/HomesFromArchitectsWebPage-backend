package sk.hfa.databases.projects.services;

import org.springframework.stereotype.Service;
import sk.hfa.databases.projects.domains.IndividualProject;
import sk.hfa.databases.projects.domains.repositories.IndividualProjectRepository;
import sk.hfa.databases.projects.services.interfaces.IIndividualProjectService;

import java.util.List;
import java.util.Optional;

@Service
public class IndividualProjectService implements IIndividualProjectService {

    private final IndividualProjectRepository individualProjectRepository;

    public IndividualProjectService(IndividualProjectRepository individualProjectRepository) {
        this.individualProjectRepository = individualProjectRepository;
    }

    @Override
    public IndividualProject findIndividualProjectById(Long id) {
        return individualProjectRepository.findById(id).orElse(null);
    }

    @Override
    public IndividualProject findIndividualProjectByProjectName(String projectName) {
        return individualProjectRepository.findByProjectName(projectName).orElse(null);
    }

    @Override
    public List<IndividualProject> getAllIndividualProjects() {
        return individualProjectRepository.findAll();
    }

    @Override
    public List<IndividualProject> saveAll(List<IndividualProject> individualProjects) {
        return individualProjectRepository.saveAll(individualProjects);
    }

    @Override
    public void save(IndividualProject project) {
        individualProjectRepository.save(project);
    }

    @Override
    public IndividualProject createNewProject(String projectName) {
        IndividualProject individualProject = new IndividualProject();
        individualProject.setProjectName(projectName);
        return individualProject;
    }
}
