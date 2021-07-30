package sk.hfa.projects.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
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

    private String hasGarage;

    private Integer persons;

    private String energeticClass;

    private Double builtUpArea;

    private Double usableArea;

    private String googleProductId;

    protected Project() {
        textSections = new ArrayList<>();
        imagePaths = new ArrayList<>();
    }

    public String getDescription() {
        if (Objects.isNull(textSections) || textSections.isEmpty())
            return null;

        StringBuilder stringBuilder = new StringBuilder();
        textSections.stream().forEach(textSection -> {
            if (!StringUtils.isBlank(textSection.getText()))
                stringBuilder.append(textSection.getText()).append("\n\n");
        });
        return stringBuilder.toString();
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
