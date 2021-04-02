package sk.hfa.projects.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);

    Optional<Project> findByTitle(String title);

    Page<Project> findAll(Pageable pageable);

    Page<Project> findAllByCategory(Category category, Pageable pageable);

    Page<Project> findByTitleStartsWith(String title, Pageable pageable);

}
