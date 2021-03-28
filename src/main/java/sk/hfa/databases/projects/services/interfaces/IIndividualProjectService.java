package sk.hfa.databases.projects.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.hfa.databases.projects.domains.IndividualProject;

import java.util.List;

public interface IIndividualProjectService {

    IndividualProject findIndividualProjectById(Long id);

    IndividualProject findIndividualProjectByProjectName(String projectName);

    Page<IndividualProject> getAllIndividualProjects(Pageable pageable);

    List<IndividualProject> saveAll(List<IndividualProject> list);

    void save(IndividualProject project);

    IndividualProject createNewProject(String projectName);
}
