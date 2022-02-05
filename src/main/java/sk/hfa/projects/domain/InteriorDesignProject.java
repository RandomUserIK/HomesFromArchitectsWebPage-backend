package sk.hfa.projects.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.enums.Category;
import sk.hfa.projects.web.domain.requestbodies.InteriorProjectRequest;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class InteriorDesignProject extends Project {

    public static Project build(InteriorProjectRequest request, Image titleImage, List<Image> galleryImages) {
        return InteriorDesignProject.builder()
                .id(request.getId())
                .title(request.getTitle())
                .titleImage(titleImage)
                .category(Category.INTERIOR_DESIGN)
                .textSections(null)
                .galleryImages(galleryImages)
                .persons(request.getPersons())
                .usableArea(request.getUsableArea())
                .build();
    }

}
