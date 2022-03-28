package sk.hfa.projects.web.domain.requestbodies;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class ProjectRequest {

    private Long id;

    @Length(max = 50,
            message = "Title must have number of chars between 0, 50")
    private String title;

    private String titleImagePath;

    @Pattern(regexp = "^(COMMON|INDIVIDUAL|INTERIOR_DESIGN)$",
            message = "Category must be one of these : COMMON, INDIVIDUAL, INTERIOR_DESIGN")
    private String category;

    @Range(min = 0,
            max = 99,
            message = "Number of persons must be in range between 0 and 99")
    private Integer persons;

    private String textSections;

    private List<String> galleryImagePaths = new ArrayList<>();

    @NotNull
    private MultipartFile titleImageFile;

    @NotNull
    private List<MultipartFile> galleryImageFiles;

}
