package sk.hfa.startup;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.hfa.images.domain.Image;
import sk.hfa.images.repositories.ImageRepository;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.TextSection;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.domain.repositories.ProjectRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class DummyIndividualProjectRunner implements CommandLineRunner {

    @Value("classpath:1.webp")
    private Resource dummyImageResource1;

    @Value("classpath:2.webp")
    private Resource dummyImageResource2;

    @Value("classpath:3.webp")
    private Resource dummyImageResource3;

    private static final String UPLOAD_PATH = System.getProperty("user.dir") + File.separator + "images" + File.separator;

    private final ProjectRepository projectRepository;

    private final ImageRepository imageRepository;

    public DummyIndividualProjectRunner(ProjectRepository projectService, ImageRepository imageRepository) {
        this.projectRepository = projectService;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws IOException {
        FileUtils.cleanDirectory(new File(UPLOAD_PATH));

        for (int i = 0; i < 20; i++) {

            Image dummyImage1 = createImage(dummyImageResource1);
            Image dummyImage2 = createImage(dummyImageResource2);
            Image dummyImage3 = createImage(dummyImageResource3);
            List<Image> dummyImages1 = Collections.singletonList(dummyImage2);
            List<Image> dummyImages2 = Collections.singletonList(dummyImage3);

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
            CommonProject commonProject = new CommonProject();
            commonProject.setCategory(Category.COMMON);
            commonProject.setTitle("Palisander " + i);
            commonProject.setPersons(6);
            commonProject.setBuiltUpArea(198.72);
            commonProject.setTotalLivingArea(231.30);
            commonProject.setEnergeticClass("A0");
            commonProject.setEntryOrientation(Arrays.asList("S", "J", "V"));
            commonProject.setSelfHelpBuildPrice(145500.0);
            commonProject.setOnKeyPrice(220000.0);
            commonProject.setBasicProjectPrice(2550.0);
            commonProject.setExtendedProjectPrice(2950.0);
            commonProject.setRooms(6);
            commonProject.setUsableArea(131.72);
            commonProject.setRoofPitch(1.5);
            commonProject.setHasGarage("Nie");
            commonProject.setMinimumParcelWidth(20.0);
            commonProject.setHeatingSource("tepelné čerpadlo");
            commonProject.setHeatingType("podlahové vykurovanie");
            commonProject.setTextSections(Arrays.asList(ts1, ts2, ts3));
            commonProject.setTitleImage(dummyImage1);
            commonProject.setGalleryImages(dummyImages1);
            commonProject.setFloorPlanImages(dummyImages2);
            commonProject = projectRepository.save(commonProject);
            log.info("New project id: " + commonProject.getId());
        }
    }

    private Image createImage(Resource dummyImage) throws IOException {
        Image image = new Image();
        image.setTitle("test1");
        image.setExtension("webp");
        image = imageRepository.save(image);
        File testFile = new File(UPLOAD_PATH + image.getId() + "." + image.getExtension());
        try (InputStream inputStream = dummyImage.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, testFile);
        }
        return image;
    }

}
