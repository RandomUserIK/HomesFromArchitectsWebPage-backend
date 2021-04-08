package sk.hfa.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.services.interfaces.IProjectService;

@Component
public class DummyIndividualProjectRunner implements CommandLineRunner {

    private final IProjectService projectService;

    public DummyIndividualProjectRunner(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 25; i++) {
            Project ip;
            if (i % 2 == 0) {
                ip = new CommonProject();
                ip.setCategory(Category.COMMON);
                ip.setPersons(4);
                ip.setOnKeyPrice(i * 200.0);
            } else {
                ip = new IndividualProject();
                ip.setCategory(Category.INDIVIDUAL);
                ip.setPersons(3);
                ip.setOnKeyPrice(i * 500.0);
            }
            ip.setTitle("Test " + i);
            projectService.save(ip);
        }
    }

}
