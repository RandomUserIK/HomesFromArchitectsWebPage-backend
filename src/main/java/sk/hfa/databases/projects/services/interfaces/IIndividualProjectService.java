package sk.hfa.databases.projects.services.interfaces;

import sk.hfa.databases.projects.domains.IndividualProject;

import java.util.List;

public interface IIndividualProjectService {

    IndividualProject findIndividualProjectById(Long Id);

    IndividualProject findIndividualProjectByProjectName(String projectName);

    List<IndividualProject> getAllIndividualProjects();

    List<IndividualProject> saveAll(List<IndividualProject> list);

    void save(IndividualProject project);

    IndividualProject createNewProject(String projectName);
}
