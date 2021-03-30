package sk.hfa.projects.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sk.hfa.projects.domain.enums.IndividualSection;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class IndividualProject extends Project {

    @Enumerated
    private IndividualSection section;

}
