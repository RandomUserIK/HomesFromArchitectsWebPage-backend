package sk.hfa.form.web.domain.requestbodies;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderFormRequest extends ContactFormRequest {

    @NotBlank
    @Length(max = 50,
            message = "Order project title must have number of chars between 0, 50")
    private String projectTitle;

    @NotBlank
    @Length(max = 35,
            message = "Order construction place must have number of chars between 0, 50")
    private String constructionPlace;

    @NotBlank
    @Length(max = 35,
            message = "Cadastral area and parcel number must have number of chars between 0, 35")
    private String cadastralAreaAndParcelNumber;

    @NotNull
    private List<String> projectType;

    @NotNull
    private List<String> additionalServices;

    @NotNull
    private List<String> connectionProjects;

}

