package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.web.domain.requestbodies.IndividualProjectRequest;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.List;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class IndividualProject extends Project {

    public static Project build(IndividualProjectRequest request, Image titleImage, List<Image> galleryImages) {
        return IndividualProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(titleImage)
                .category(Category.INDIVIDUAL)
                .textSections(request.getTextSections())
                .galleryImages(galleryImages)
                .hasGarage(request.getHasGarage())
                .persons(request.getPersons())
                .energeticClass(request.getEnergeticClass())
                .builtUpArea(request.getBuiltUpArea())
                .usableArea(request.getUsableArea())
                .build();
    }

}
