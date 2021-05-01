package sk.hfa.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.TextSection;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.services.interfaces.IProjectService;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Component
public class DummyIndividualProjectRunner implements CommandLineRunner {

    private final IProjectService projectService;

    public DummyIndividualProjectRunner(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void run(String... args) {
        TextSection ts1 = new TextSection();
        ts1.setTitle("");
        ts1.setText("Rodinný dom Palisander patrí medzi nadštandardné dvojpodlažné šesťizbové rodinné domy. " +
                "Určený je pre bývanie až šiestich členov rodiny. Je ideálny na rovinatý prípadne mierne svahovitý pozemok. " +
                "Tento rodinný dom je samotne stojaci objekt, bez podpivničenia s plochou strechou.");
        TextSection ts2 = new TextSection();
        ts2.setTitle("Dispozícia");
        ts2.setText("Na spodnom podlaží vchádzame cez prestrešený vstup do vstupnej haly. " +
                "Nachádza sa tu hosťovská izba s vlastnou kúpeľnou, WC, technická miestnosť s komorou, " +
                "veľký otvorený šatník a denný priestor s polooddelenou jedálenskou časťou. Schodiskom vchádzame na " +
                "druhé nadzemné podlažie, kde sú dve izby s vlastnou kúpeľňou a plne vybavený spálňový apartmán. " +
                "Pod vykonzolovanou časťou domu sa nachádza polokrytá garáž pre dve autá s miestom na stolovanie " +
                "alebo iné aktivity pod strechou.'");
        TextSection ts3 = new TextSection();
        ts3.setTitle("Technické riešenie");
        ts3.setText("Rodinný dom je navrhnutý z keramických tvárnic, ktoré možno zameniť za pórobetón. " +
                "Stropy sú monolitické betónové. Vykurovanie je podlahové.");
        Project commonProject = new CommonProject();
        commonProject.setCategory(Category.COMMON);
        commonProject.setTitle("Palisander");
        commonProject.setPersons(6);
        commonProject.setBuiltUpArea(198.72);
        ((CommonProject) commonProject).setTotalLivingArea(231.30);
        commonProject.setEnergeticClass("A0");
        ((CommonProject) commonProject).setEntryOrientation("S, V, Z");
        ((CommonProject) commonProject).setSelfHelpBuildPrice(145500.0);
        ((CommonProject) commonProject).setOnKeyPrice(220000.0);
        ((CommonProject) commonProject).setBasicProjectPrice(2550.0);
        ((CommonProject) commonProject).setExtendedProjectPrice(2950.0);
        ((CommonProject) commonProject).setRooms(6);
        commonProject.setUsableArea(131.72);
        ((CommonProject) commonProject).setRoofPitch(1.5);
        ((CommonProject) commonProject).setMinimumParcelWidth(20.0);
        ((CommonProject) commonProject).setHeatingSource("tepelné čerpadlo");
        ((CommonProject) commonProject).setHeatingType("podlahové vykurovanie");
        ((CommonProject) commonProject).setFloorPlanImagePaths(Collections.singletonList("floor_plan.png"));
        commonProject.setImagePaths(Arrays.asList("slide1.png", "slide2.png"));
        commonProject.setTextSections(Arrays.asList(ts1, ts2, ts3));
        commonProject = projectService.save(commonProject);
        log.info("New project id: " + commonProject.getId());
    }

}
