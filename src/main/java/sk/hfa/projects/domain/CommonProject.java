package sk.hfa.projects.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CommonProject extends Project {

    private Boolean hasGarage;

    private Integer persons;

    private Integer rooms;

    private String energeticClass;

    private String entryOrientation;

    private String heatingSource;

    private String heatingType;

    private String floorPlanImage;

    private Double builtUpArea;

    private Double usableArea;

    private Double selfHelpBuildPrice;

    private Double onKeyPrice;

    private Double basicProjectPrice;

    private Double extendedProjectPrice;

    private Double totalLivingArea;

    private Double roofPitch;

    private Double minimumParcelWidth;

}
