package sk.hfa.projects.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.util.ProjectUtils;
import sk.hfa.projects.web.domain.requestbodies.CommonProjectRequest;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CommonProject extends IndividualProject {

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> floorPlanImages;

    public static Project build(CommonProjectRequest request,
                                Image titleImage,
                                List<Image> galleryImages,
                                List<Image> floorPlanImages) {
        return CommonProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(titleImage)
                .category(Category.COMMON)
                .textSections(ProjectUtils.readTextSections(request.getTextSections()))
                .galleryImages(galleryImages)
                .hasGarage(request.getHasGarage())
                .hasStorey(request.getHasStorey())
                .persons(request.getPersons())
                .rooms(request.getRooms())
                .energeticClass(request.getEnergeticClass())
                .entryOrientation(request.getEntryOrientation())
                .heatingSource(request.getHeatingSource())
                .heatingType(request.getHeatingType())
                .floorPlanImages(floorPlanImages)
                .builtUpArea(request.getBuiltUpArea())
                .usableArea(request.getUsableArea())
                .selfHelpBuildPrice(request.getSelfHelpBuildPrice())
                .onKeyPrice(request.getOnKeyPrice())
                .basicProjectPrice(request.getBasicProjectPrice())
                .extendedProjectPrice(request.getExtendedProjectPrice())
                .totalLivingArea(request.getTotalLivingArea())
                .roofPitch(request.getRoofPitch())
                .minimumParcelWidth(request.getMinimumParcelWidth())
                .build();
    }

}