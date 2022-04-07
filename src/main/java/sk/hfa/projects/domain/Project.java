package sk.hfa.projects.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import sk.hfa.images.domain.Image;
import sk.hfa.projects.domain.enums.Category;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    private Image titleImage;

    @Enumerated
    private Category category;

    private Integer persons;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TextSection> textSections;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> galleryImages;

}
