package sk.hfa.projects.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.enums.Category;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @OneToOne
    private Image titleImage;

    @Enumerated
    private Category category;

    // TODO how is this field set
    private String description;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TextSection> textSections;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Image> galleryImages;

    private String hasGarage;

    private Integer persons;

    private String energeticClass;

    private Double builtUpArea;

    private Double usableArea;

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (Objects.isNull(other) || getClass() != other.getClass())
            return false;

        Project project = (Project) other;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
