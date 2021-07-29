package sk.hfa.google.products.services.interfaces;

import sk.hfa.projects.domain.Project;

public interface IGoogleProductsService {

    void createGoogleProduct(Project project);

    void removeGoogleProduct(String productId);

}
