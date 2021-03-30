package sk.hfa.projects.domain;

import lombok.Data;
import sk.hfa.projects.domain.enums.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String titleImage;

    @Enumerated
    private Category category;

    @OneToMany
    private List<TextSection> textSections;

    @ElementCollection
    private List<String> imagePaths;

    protected Project() {
        textSections = new ArrayList<>();
        imagePaths = new ArrayList<>();
    }

}
