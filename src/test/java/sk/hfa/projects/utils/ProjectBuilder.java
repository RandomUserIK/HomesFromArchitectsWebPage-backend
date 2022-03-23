package sk.hfa.projects.utils;

import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.*;
import sk.hfa.projects.domain.enums.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ProjectBuilder {

    public static Project getInteriorProject() {
        InteriorDesignProject interiorDesignProject = new InteriorDesignProject();
        return buildInteriorProject(interiorDesignProject);
    }

    public static Project getIndividualProject() {
        IndividualProject individualProject = new IndividualProject();
        return buildIndividualProject(individualProject);
    }

    public static Project getCommonProject() {
        CommonProject commonProject = new CommonProject();
        return buildCommonProject(commonProject);
    }

    private static void buildProject(Project project) {
        project.setTitle("Test project");
        project.setPersons(4);
        project.setTitleImage(getImage("Title image"));
        project.setGalleryImages(getImages());
        project.setTextSections(getTextSections());
    }

    public static Project buildIndividualProject(IndividualProject individualProject) {
        buildProject(individualProject);
        individualProject.setCategory(Category.INDIVIDUAL);
        individualProject.setBuiltUpArea(222.22);
        individualProject.setUsableArea(122.22);
        individualProject.setEnergeticClass("A1");
        individualProject.setHasGarage("Áno");
        return individualProject;
    }

    public static Project buildCommonProject(CommonProject commonProject) {
        buildIndividualProject(commonProject);
        commonProject.setCategory(Category.COMMON);
        commonProject.setTotalLivingArea(231.30);
        commonProject.setEntryOrientation(Arrays.asList("S", "J", "V"));
        commonProject.setSelfHelpBuildPrice(145500.0);
        commonProject.setOnKeyPrice(220000.0);
        commonProject.setBasicProjectPrice(2550.0);
        commonProject.setExtendedProjectPrice(2950.0);
        commonProject.setRooms(6);
        commonProject.setRoofPitch(1.5);
        commonProject.setMinimumParcelWidth(20.0);
        commonProject.setFloorPlanImages(getImages());
        return commonProject;
    }

    public static Project buildInteriorProject(InteriorDesignProject interiorDesignProject) {
        buildProject(interiorDesignProject);
        interiorDesignProject.setCategory(Category.INTERIOR_DESIGN);
        return interiorDesignProject;
    }

    private static List<Image> getImages() {
        List<Image> images = new ArrayList<>();
        IntStream.range(0, 4).forEach((i) -> images.add(getImage("Title " + i)));
        return images;
    }

    private static Image getImage(String title) {
        Image image = new Image();
        image.setTitle(title);
        image.setExtension("jpg");
        return image;
    }

    private static List<TextSection> getTextSections() {
        List<TextSection> textSections = new ArrayList<>();
        IntStream.range(0, 4).forEach((i) -> textSections.add(getTextSection()));
        return textSections;
    }

    private static TextSection getTextSection() {
        TextSection textSection = new TextSection();
        textSection.setTitle("Dispozícia");
        textSection.setText("Na spodnom podlaží vchádzame cez prestrešený vstup do vstupnej haly. " +
                "Nachádza sa tu hosťovská izba s vlastnou kúpeľnou, WC, technická miestnosť s komorou, " +
                "veľký otvorený šatník a denný priestor s polooddelenou jedálenskou časťou. Schodiskom vchádzame na " +
                "druhé nadzemné podlažie, kde sú dve izby s vlastnou kúpeľňou a plne vybavený spálňový apartmán. " +
                "Pod vykonzolovanou časťou domu sa nachádza polokrytá garáž pre dve autá s miestom na stolovanie " +
                "alebo iné aktivity pod strechou.'");
        return textSection;
    }


}
