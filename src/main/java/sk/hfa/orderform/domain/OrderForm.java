package sk.hfa.orderform.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.hfa.orderform.web.domain.requestbodies.OrderFormRequest;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String projectTitle;

    private String constructionPlace;

    private String cadastralAreaAndParcelNumber;

    @ElementCollection
    private List<String> projectType;

    @ElementCollection
    private List<String> additionalServices;

    @ElementCollection
    private List<String> connectionProjects;


    public static OrderForm build(OrderFormRequest request) {
        return OrderForm.builder()
                .projectTitle(request.getProjectTitle())
                .constructionPlace(request.getConstructionPlace())
                .cadastralAreaAndParcelNumber(request.getCadastralAreaAndParcelNumber())
                .projectType(request.getProjectType())
                .additionalServices(request.getAdditionalServices())
                .connectionProjects(request.getConnectionProjects())
                .build();
    }
}
