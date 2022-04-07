package sk.hfa.projects.utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.IndividualProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.InteriorProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ProjectRequestBuilder {

    public static ProjectRequest getInteriorProjectRequest() {
        InteriorProjectRequest interiorProjectRequest = new InteriorProjectRequest();
        return buildInteriorProjectRequest(interiorProjectRequest);
    }

    public static ProjectRequest getIndividualProjectRequest() {
        IndividualProjectRequest individualProject = new IndividualProjectRequest();
        return buildIndividualProjectRequest(individualProject);
    }

    public static ProjectRequest getCommonProjectRequest() {
        CommonProjectRequest commonProjectRequest = new CommonProjectRequest();
        return buildCommonProjectRequest(commonProjectRequest);
    }

    private static void buildProject(ProjectRequest project) {
        project.setId(1L);
        project.setTitle("Test project");
        project.setPersons(4);
        project.setTitleImageFile(getImage("Title image"));
        project.setGalleryImageFiles(getImages());
        project.setTextSections("[{\"title\":\"Title 1\",\"text\":\"Text 1\"},{\"title\":\"Title 2\",\"text\":\"Text 2\"}]");
    }

    private static ProjectRequest buildIndividualProjectRequest(IndividualProjectRequest individualProject) {
        buildProject(individualProject);
        individualProject.setCategory("INDIVIDUAL");
        individualProject.setBuiltUpArea(222.22);
        individualProject.setUsableArea(122.22);
        individualProject.setEnergeticClass("A1");
        individualProject.setHasGarage("Áno");
        return individualProject;
    }

    private static ProjectRequest buildCommonProjectRequest(CommonProjectRequest commonProject) {
        buildIndividualProjectRequest(commonProject);
        commonProject.setCategory("COMMON");
        commonProject.setTotalLivingArea(231.30);
        commonProject.setEntryOrientation(Arrays.asList("S", "J", "V"));
        commonProject.setSelfHelpBuildPrice(145500.0);
        commonProject.setOnKeyPrice(220000.0);
        commonProject.setBasicProjectPrice(2550.0);
        commonProject.setExtendedProjectPrice(2950.0);
        commonProject.setRooms(6);
        commonProject.setRoofPitch(1.5);
        commonProject.setMinimumParcelWidth(20.0);
        commonProject.setFloorPlanImageFiles(getImages());
        return commonProject;
    }

    private static ProjectRequest buildInteriorProjectRequest(InteriorProjectRequest interiorDesignProject) {
        buildProject(interiorDesignProject);
        interiorDesignProject.setCategory("INTERIOR_DESIGN");
        return interiorDesignProject;
    }

    private static List<MultipartFile> getImages() {
        List<MultipartFile> images = new ArrayList<>();
        IntStream.range(0, 4).forEach((i) -> images.add(getImage("Title " + i)));
        return images;
    }

    private static MockMultipartFile getImage(String name) {
        return new MockMultipartFile(
                name,
                "hello.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
    }

}
