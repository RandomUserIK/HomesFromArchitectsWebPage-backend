package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import sk.hfa.projects.domain.enums.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String titleImage;

    @Enumerated
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TextSection> textSections;

    @ElementCollection
    private List<String> imagePaths;

    private Boolean hasGarage;

    private Integer persons;

    private Integer rooms;

    private String energeticClass;

    private String entryOrientation;

    private String heatingSource;

    private String heatingType;

    @ElementCollection
    private List<String> floorPlanImagesPaths;

    private Double builtUpArea;

    private Double usableArea;

    private Double selfHelpBuildPrice;

    private Double onKeyPrice;

    private Double basicProjectPrice;

    private Double extendedProjectPrice;

    private Double totalLivingArea;

    private Double roofPitch;

    private Double minimumParcelWidth;

    protected Project() {
        textSections = new ArrayList<>();
        imagePaths = new ArrayList<>();
    }

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
