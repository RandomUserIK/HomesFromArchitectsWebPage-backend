package sk.hfa.order.web.domain.requestbodies;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String projectTitle;
    private String constructionPlace;
    private String cadastralAreaAndParcelNumber;
    private List<String> projectType;
    private List<String> additionalServices;
    private List<String> connectionProjects;

}
