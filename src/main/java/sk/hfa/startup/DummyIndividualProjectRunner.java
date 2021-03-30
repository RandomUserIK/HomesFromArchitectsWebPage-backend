package sk.hfa.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DummyIndividualProjectRunner implements CommandLineRunner {

    // private final IIndividualProjectService projectService;
    //
    // public DummyIndividualProjectRunner(IIndividualProjectService projectService) {
    //     this.projectService = projectService;
    // }

    @Override
    public void run(String... args) throws Exception {
        // for (int i = 0; i < 25; i++) {
        //     IndividualProject ip = new IndividualProject();
        //     ip.setProjectName("Project name "+ i);
        //     ip.setBuiltUpArea(2222.2);
        //     ip.setPersons(4);
        //     ip.setGarage(true);
        //     ip.setRooms(8);
        //     ip.setEnergeticClass("Test energetic class");
        //     ip.setOrientation("Test orientation");
        //     ip.setGrossArea(22.22);
        //     ip.setSelfHelpBuild(22.222);
        //     ip.setPriceOnKey(8888.88);
        //     ip.setBasicProject(7777.77);
        //     ip.setAdvancedProject(7778.77);
        //     ip.setTitlePhoto("");
        //     ip.setFloorPlanPhoto("");
        //     ip.setAngleOfRoof(60.0);
        //     ip.setHeatingSource("Test heating source");
        //     ip.setHeatingType("Test heating type");
        //     projectService.save(ip);
        // }
    }
}
