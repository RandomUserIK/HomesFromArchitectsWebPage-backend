package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.web.domain.requestbodies.InteriorProjectRequest;
import sk.hfa.projects.web.domain.requestbodies.ProjectRequest;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class InteriorDesignProject extends Project {

    // TODO:
    private boolean interior;

    public static Project build(ProjectRequest request) {
        return InteriorDesignProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(request.getTitleImage())
                .category(Category.INTERIOR_DESIGN)
                .textSections(request.getTextSections())
                .imagePaths(request.getImagePaths())
                .hasGarage(request.getHasGarage())
                .persons(request.getPersons())
                .energeticClass(request.getEnergeticClass())
                .builtUpArea(request.getBuiltUpArea())
                .usableArea(request.getUsableArea())
                .interior(((InteriorProjectRequest) request).isInterior())
                .build();
    }

}
