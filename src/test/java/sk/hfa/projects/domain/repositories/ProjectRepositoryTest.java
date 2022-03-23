package sk.hfa.projects.domain.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import sk.hfa.projects.domain.*;
import sk.hfa.projects.utils.ProjectBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sk.hfa.util.Constants.ELEMENTS_PER_PAGE;

@DataJpaTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;


    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
    }

    @Test
    void testFindById_shouldReturnOne() {
        Project project = ProjectBuilder.getInteriorProject();
        project = projectRepository.save(project);
        projectRepository.findById(project.getId());
        assertNotNull(projectRepository.findById(project.getId()));
    }

    @Test
    void testFindAll_shouldReturnThree() {
        projectRepository.saveAll(Arrays.asList(ProjectBuilder.getCommonProject(),
                ProjectBuilder.getIndividualProject(),
                ProjectBuilder.getInteriorProject()));
        List<Project> projects = projectRepository.findAll();
        assertEquals(3, projects.size());
    }

    @Test
    void testFindAllWithPageable_getOnePage_shouldReturn10() {
        initProjects();
        PageRequest pageable = PageRequest.of(0, ELEMENTS_PER_PAGE);
        Page<Project> projects = projectRepository.findAll(pageable);
        assertEquals(10, projects.getSize());
    }

    @Test
    void testFindAllWithPageablePredicate_searchByTitle_shouldReturn1() {
        initProjects();
        Project project = ProjectBuilder.getCommonProject();
        project.setTitle("aHOJšŠťŤx");
        projectRepository.save(project);
        PageRequest pageable = PageRequest.of(0, ELEMENTS_PER_PAGE);
        Page<Project> projects = projectRepository.findAll(QProject.project.title.equalsIgnoreCase("ahOJšŠťŤx"), pageable);
        assertEquals(1, projects.getContent().size());
    }

    @Test
    void testFindAllWithPageablePredicate_searchByPersons_shouldReturn1() {
        initProjects();
        Project project = ProjectBuilder.getCommonProject();
        project.setPersons(44);
        projectRepository.save(project);
        PageRequest pageable = PageRequest.of(0, ELEMENTS_PER_PAGE);
        Page<Project> projects = projectRepository.findAll(QProject.project.persons.eq(44), pageable);
        assertEquals(1, projects.getContent().size());
    }

    @Test
    void testFindAllWithPageablePredicate_searchByHasGarage_shouldReturn1() {
        initProjects();
        Project project = ProjectBuilder.getCommonProject();
        ((CommonProject) project).setHasGarage("Nie");
        projectRepository.save(project);
        PageRequest pageable = PageRequest.of(0, ELEMENTS_PER_PAGE);
        Page<Project> projects = projectRepository.findAll(QProject.project.as(QCommonProject.class).hasGarage.eq("Áno"), pageable);
        assertEquals(10, projects.getContent().size());
    }

    private void initProjects() {
        IntStream.range(0, 30).forEach((i) -> {
            projectRepository.saveAll(Arrays.asList(ProjectBuilder.getCommonProject(),
                    ProjectBuilder.getIndividualProject(),
                    ProjectBuilder.getInteriorProject()));
        });
    }

}