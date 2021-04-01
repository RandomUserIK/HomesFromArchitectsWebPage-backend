package sk.hfa.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.hfa.projects.domain.IndividualProject;
import sk.hfa.projects.domain.Project;
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
            Project ip = new IndividualProject();
            ip.setTitle("Test" + i);
            projectService.save(ip);
        }
    }
}
