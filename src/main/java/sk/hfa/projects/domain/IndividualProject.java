package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.util.ProjectUtils;
import sk.hfa.projects.web.domain.requestbodies.IndividualProjectRequest;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class IndividualProject extends Project {

    private Double builtUpArea;

    private Double usableArea;

    private String energeticClass;

    private String hasGarage;


    public static Project build(IndividualProjectRequest request, Image titleImage, List<Image> galleryImages) {
        return IndividualProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(titleImage)
                .category(Category.INDIVIDUAL)
                .textSections(ProjectUtils.readTextSections(request.getTextSections()))
                .galleryImages(galleryImages)
                .hasGarage(request.getHasGarage())
                .persons(request.getPersons())
                .energeticClass(request.getEnergeticClass())
                .builtUpArea(request.getBuiltUpArea())
                .usableArea(request.getUsableArea())
                .build();
    }

}
