package sk.hfa.projects.web.domain.requestbodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CommonProjectRequest extends ProjectRequest {

    private Integer rooms;

    private List<String> entryOrientation;

    private String heatingSource;

    private String heatingType;

    private String hasStorey;

    private Double selfHelpBuildPrice;

    private Double onKeyPrice;

    private Double basicProjectPrice;

    private Double extendedProjectPrice;

    private Double totalLivingArea;

    private Double roofPitch;

    private Double minimumParcelWidth;

    private List<MultipartFile> floorPlanImageFiles = new ArrayList<>();

}
