package sk.hfa.projects.web.domain.requestbodies;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import sk.hfa.projects.domain.TextSection;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        property = "category")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommonProjectRequest.class, name = "COMMON"),
        @JsonSubTypes.Type(value = IndividualProjectRequest.class, name = "INDIVIDUAL"),
        @JsonSubTypes.Type(value = InteriorProjectRequest.class, name = "INTERIOR_DESIGN")
})
public abstract class ProjectRequest {

    private Long id;

    @Length(min = 0,
            max = 50,
            message = "Title must have number of chars between 0, 50")
    private String title;

    private String titleImage;

    @Pattern(regexp = "^(COMMON|INDIVIDUAL|INTERIOR_DESIGN)$",
            message = "Category must be one of these : COMMON, INDIVIDUAL, INTERIOR_DESIGN")
    private String category;

    @Pattern(regexp = "^(A1|A0|A|B|C|D|E|F|G)$",
            message = "Energetic class must be in range from A to G or A0 / A1")
    private String energeticClass;

    @Pattern(regexp = "Áno|Nie",
            flags = Pattern.Flag.CANON_EQ,
            message = "Project garage question answer must be Áno or Nie")
    private String hasGarage;

    @Range(min = 0,
            max = 99,
            message = "Number of persons must be in range between 0 and 99")
    private Integer persons;

    @Range(min = 0,
            max = 999999,
            message = "Built up area must be in range between 0 and 999999")
    private Double builtUpArea;

    @Range(min = 0,
            max = 999999,
            message = "Usable area must be in range between 0 and 999999")
    private Double usableArea;

    private List<TextSection> textSections = new ArrayList<>();

    private List<String> imagePaths = new ArrayList<>();

}
