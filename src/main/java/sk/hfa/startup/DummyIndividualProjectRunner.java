package sk.hfa.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.TextSection;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.services.interfaces.IProjectService;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class DummyIndividualProjectRunner implements CommandLineRunner {

    @Value("classpath:title.png")
    private Resource titleImage;

    private final IProjectService projectService;

    public DummyIndividualProjectRunner(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void run(String... args) throws IOException {
        for (int i = 0; i < 20; i++) {
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
            commonProject.setTitle("Palisander " + i);
            commonProject.setPersons(6);
            commonProject.setBuiltUpArea(198.72);
            ((CommonProject) commonProject).setTotalLivingArea(231.30);
            commonProject.setEnergeticClass("A0");
            ((CommonProject) commonProject).setEntryOrientation(Arrays.asList("S", "J", "V"));
            ((CommonProject) commonProject).setSelfHelpBuildPrice(145500.0);
            ((CommonProject) commonProject).setOnKeyPrice(220000.0);
            ((CommonProject) commonProject).setBasicProjectPrice(2550.0);
            ((CommonProject) commonProject).setExtendedProjectPrice(2950.0);
            ((CommonProject) commonProject).setRooms(6);
            commonProject.setUsableArea(131.72);
            commonProject.setTitleImage(titleImage.getFile().getPath());
            ((CommonProject) commonProject).setRoofPitch(1.5);
            commonProject.setHasGarage("Nie");
            ((CommonProject) commonProject).setMinimumParcelWidth(20.0);
            ((CommonProject) commonProject).setHeatingSource("tepelné čerpadlo");
            ((CommonProject) commonProject).setHeatingType("podlahové vykurovanie");
            ((CommonProject) commonProject).setFloorPlanImagePaths(Arrays.asList(titleImage.getFile().getPath(), titleImage.getFile().getPath()));
            commonProject.setImagePaths(Arrays.asList(titleImage.getFile().getPath(), titleImage.getFile().getPath()));
            commonProject.setTextSections(Arrays.asList(ts1, ts2, ts3));
            projectService.save(commonProject);
        }
    }

}
