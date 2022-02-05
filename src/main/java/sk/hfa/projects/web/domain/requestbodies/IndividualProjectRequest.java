package sk.hfa.projects.web.domain.requestbodies;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
public class IndividualProjectRequest extends ProjectRequest {

    @Range(min = 0,
            max = 999999999,
            message = "Built up area must be in range between 0 and 999999999")
    private Double builtUpArea;

    @Range(min = 0,
            max = 999999999,
            message = "Usable area must be in range between 0 and 999999999")
    private Double usableArea;

    @Pattern(regexp = "^(A1|A0|A|B|C|D|E|F|G)$",
            message = "Energetic class must be in range from A to G or A0 / A1")
    private String energeticClass;

    @Pattern(regexp = "^(Áno|Nie)$",
            message = "Project garage question answer must be Áno or Nie")
    private String hasGarage;

}
