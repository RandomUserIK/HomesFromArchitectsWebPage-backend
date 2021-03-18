package sk.hfa.databases.projects.domains.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.databases.projects.domains.IndividualProject;

import java.util.Optional;


@Repository
public interface IndividualProjectRepository extends JpaRepository<IndividualProject, Long> {

    Optional<IndividualProject> findById(Long id);

    Optional<IndividualProject> findByProjectName(String projectName);
}
