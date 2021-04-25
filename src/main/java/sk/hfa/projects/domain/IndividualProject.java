package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.web.domain.requestbodies.IndividualProjectRequest;
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
public class IndividualProject extends Project {

    @ElementCollection
    private List<String> galleryImagePaths;

    public IndividualProject() {
        galleryImagePaths = new ArrayList<>();
    }

    public static Project build(ProjectRequest request) {
        return IndividualProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(request.getTitleImage())
                .category(Category.INDIVIDUAL)
                .textSections(request.getTextSections())
                .imagePaths(request.getImagePaths())
                .hasGarage(request.getHasGarage())
                .persons(request.getPersons())
                .energeticClass(request.getEnergeticClass())
                .builtUpArea(request.getBuiltUpArea())
                .usableArea(request.getUsableArea())
                .galleryImagePaths(((IndividualProjectRequest) request).getGalleryImagePaths())
                .build();
    }

}
