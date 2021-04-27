package sk.hfa.orderform.web.domain.requestbodies;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderFormRequest {

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
