package sk.hfa.projects.web.domain.requestbodies;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.hfa.projects.domain.TextSection;

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

    private String title;

    private String titleImage;

    private String category;

    private String energeticClass;

    private String hasGarage;

    private Integer persons;

    private Double builtUpArea;

    private Double usableArea;

    private List<TextSection> textSections = new ArrayList<>();

    private List<String> imagePaths = new ArrayList<>();

}
