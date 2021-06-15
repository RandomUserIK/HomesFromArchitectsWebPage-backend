package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CommonProject extends Project {

    private Integer rooms;

    @ElementCollection
    private List<String> entryOrientation;

    private String heatingSource;

    private String heatingType;

    private String hasStorey;

    private Double selfHelpBuildPrice;

    private Double onKeyPrice;

    private Double basicProjectPrice;

    private Double extendedProjectPrice;

    private Double totalLivingArea;

    private Double roofPitch;

    private Double minimumParcelWidth;

    @ElementCollection
    private List<String> floorPlanImagePaths;

    public CommonProject() {
        floorPlanImagePaths = new ArrayList<>();
    }

    public static Project build(ProjectRequest request) {
        return CommonProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(request.getTitleImage())
                .category(Category.COMMON)
                .textSections(request.getTextSections())
                .imagePaths(request.getImagePaths())
                .hasGarage(request.getHasGarage())
                .hasStorey(((CommonProjectRequest) request).getHasStorey())
                .persons(request.getPersons())
                .rooms(((CommonProjectRequest) request).getRooms())
                .energeticClass(request.getEnergeticClass())
                .entryOrientation(((CommonProjectRequest) request).getEntryOrientation())
                .heatingSource(((CommonProjectRequest) request).getHeatingSource())
                .heatingType(((CommonProjectRequest) request).getHeatingType())
                .floorPlanImagePaths(((CommonProjectRequest) request).getFloorPlanImagePaths())
                .builtUpArea(request.getBuiltUpArea())
                .usableArea(request.getUsableArea())
                .selfHelpBuildPrice(((CommonProjectRequest) request).getSelfHelpBuildPrice())
                .onKeyPrice(((CommonProjectRequest) request).getOnKeyPrice())
                .basicProjectPrice(((CommonProjectRequest) request).getBasicProjectPrice())
                .extendedProjectPrice(((CommonProjectRequest) request).getExtendedProjectPrice())
                .totalLivingArea(((CommonProjectRequest) request).getTotalLivingArea())
                .roofPitch(((CommonProjectRequest) request).getRoofPitch())
                .minimumParcelWidth(((CommonProjectRequest) request).getMinimumParcelWidth())
                .build();
    }

}