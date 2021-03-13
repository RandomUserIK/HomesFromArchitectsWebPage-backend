package sk.hfa.databases.posts.services.interfaces;

import sk.hfa.databases.posts.domains.IndividualProject;

import java.util.List;

public interface IIndividualProjectService {

    IndividualProject findIndividualProjectById(Long Id);

    List<IndividualProject> getAllIndividualProjects();

    List<IndividualProject> saveAll(List<IndividualProject> list);
}
