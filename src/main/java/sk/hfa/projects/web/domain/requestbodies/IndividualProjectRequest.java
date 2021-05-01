package sk.hfa.projects.web.domain.requestbodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IndividualProjectRequest extends ProjectRequest {

    private List<String> galleryImagePaths;

    public IndividualProjectRequest() {
        galleryImagePaths = new ArrayList<>();
    }

}
