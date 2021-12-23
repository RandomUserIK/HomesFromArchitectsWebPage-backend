package sk.hfa.google.products.services.interfaces;

import sk.hfa.projects.domain.Project;

public interface IGoogleProductsService {

    String createGoogleProduct(Project project);

    void removeGoogleProduct(String googleProductId);

}
