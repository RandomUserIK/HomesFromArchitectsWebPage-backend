package sk.hfa.databases.posts.services;

import org.springframework.stereotype.Service;
import sk.hfa.databases.posts.domains.IndividualProject;
import sk.hfa.databases.posts.domains.repositories.IndividualProjectRepository;
import sk.hfa.databases.posts.services.interfaces.IIndividualProjectService;

import java.util.List;
import java.util.Optional;

@Service
public class IndividualProjectService implements IIndividualProjectService {

    private final IndividualProjectRepository individualProjectRepository;

    public IndividualProjectService(IndividualProjectRepository individualProjectRepository) {
        this.individualProjectRepository = individualProjectRepository;
    }

    @Override
    public IndividualProject findIndividualProjectById(Long Id) {
        Optional<IndividualProject> post = individualProjectRepository.findById(Id);
        return post.orElse(null);
    }

    @Override
    public List<IndividualProject> getAllIndividualProjects() {
        return individualProjectRepository.findAll();
    }

    @Override
    public List<IndividualProject> saveAll(List<IndividualProject> individualProjects) {
        return individualProjectRepository.saveAll(individualProjects);
    }
}
