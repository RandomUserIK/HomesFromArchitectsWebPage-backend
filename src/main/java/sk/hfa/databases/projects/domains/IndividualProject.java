package sk.hfa.databases.projects.domains;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class IndividualProject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String projectName;
    private Double builtUpArea;
    private Integer persons;
    private Boolean garage;
    private Integer rooms;
    private String energeticClass;
    private String orientation;
    private Double grossArea;
    private Double selfHelpBuild;
    private Double priceOnKey;
    private Double basicProject;
    private Double advancedProject;
    private String titlePhoto;
    private String floorPlanPhoto;
    private Double totalLivingArea;
    private Double angleOfRoof;
    private Double minimumParcelWidth;
    private String heatingSource;
    private String heatingType;
    @OneToMany()
    private List<TextSections> textSections;
    @ElementCollection
    private List<String> photoPaths = new ArrayList();
}

