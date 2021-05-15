package sk.hfa.projects.web.domain.requestbodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonProjectRequest extends ProjectRequest {

    private Integer rooms;

    private List<String> entryOrientation;

    private String heatingSource;

    private String heatingType;

    private Double selfHelpBuildPrice;

    private Double onKeyPrice;

    private Double basicProjectPrice;

    private Double extendedProjectPrice;

    private Double totalLivingArea;

    private Double roofPitch;

    private Double minimumParcelWidth;

    private List<String> floorPlanImagePaths = new ArrayList<>();

}
