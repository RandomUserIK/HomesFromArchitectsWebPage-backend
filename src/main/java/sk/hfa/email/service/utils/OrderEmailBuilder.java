package sk.hfa.email.service.utils;

import lombok.Data;
import sk.hfa.form.web.domain.requestbodies.OrderFormRequest;

@Data
public class OrderEmailBuilder {

    private static String buildContactInfo(OrderFormRequest orderFormRequest) {
        return ContactEmailBuilder.build(orderFormRequest);
    }

    public static String build(OrderFormRequest orderFormRequest) {
        String newLine = System.getProperty("line.separator");

        return buildContactInfo(orderFormRequest) +
                newLine +
                newLine +
                "Názov projektu: " + orderFormRequest.getProjectTitle() +
                newLine +
                "Miesto výstavby: " + orderFormRequest.getConstructionPlace() +
                newLine +
                "Katastrálne územie / parcelné číslo: " + orderFormRequest.getCadastralAreaAndParcelNumber() +
                newLine +
                "Typ projektu: " + orderFormRequest.getProjectType() +
                newLine +
                "Doplnkové služby: " + orderFormRequest.getAdditionalServices() +
                newLine +
                "Projekty prípojok: " + orderFormRequest.getConnectionProjects();
    }

}
