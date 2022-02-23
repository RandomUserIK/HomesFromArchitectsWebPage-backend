package sk.hfa.projects.web.domain.requestbodies;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonProjectRequest extends IndividualProjectRequest {

    @Range(min = 0,
            max = 99,
            message = "Number of persons must be in range between 0 and 99")
    private Integer rooms;

    @NotNull
    private List<String> entryOrientation;

    @NotNull
    private String heatingSource;

    @NotNull
    private String heatingType;

    @Pattern(regexp = "^(Áno|Nie)$",
            flags = Pattern.Flag.CANON_EQ,
            message = "Project garage question answer must be Áno or Nie")
    private String hasStorey;

    @Range(min = 0,
            max = 999999999,
            message = "Self help build price up must be in range between 0 and 999999999")
    private Double selfHelpBuildPrice;

    @Range(min = 0,
            max = 999999999,
            message = "On key price must be in range between 0 and 999999999")
    private Double onKeyPrice;

    @Range(min = 0,
            max = 999999999,
            message = "Basic project price must be in range between 0 and 999999999")
    private Double basicProjectPrice;

    @Range(min = 0,
            max = 999999999,
            message = "Extended project price must be in range between 0 and 999999999")
    private Double extendedProjectPrice;

    @Range(min = 0,
            max = 999999999,
            message = "Total living area must be in range between 0 and 999999999")
    private Double totalLivingArea;

    @Range(min = 0,
            max = 999999999,
            message = "Roof pitch must be in range between 0 and 999999999")
    private Double roofPitch;

    @Range(min = 0,
            max = 999999999,
            message = "Minimum parcel width must be in range between 0 and 999999999")
    private Double minimumParcelWidth;

    @NotNull
    private List<MultipartFile> floorPlanImageFiles = new ArrayList<>();

}
