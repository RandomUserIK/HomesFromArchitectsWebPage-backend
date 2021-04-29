package sk.hfa.form.web.domain.requestbodies;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderFormRequest extends ContactFormRequest {

    @NotBlank
    private String projectTitle;

    @NotBlank
    private String constructionPlace;

    @NotBlank
    private String cadastralAreaAndParcelNumber;

    @NotNull
    private List<String> projectType;

    @NotNull
    private List<String> additionalServices;

    @NotNull
    private List<String> connectionProjects;

}

