package sk.hfa.google.products.services;

import com.google.api.services.content.model.CustomAttribute;
import com.google.api.services.content.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import sk.hfa.google.products.services.interfaces.IGoogleProductsClientService;
import sk.hfa.google.products.services.interfaces.IGoogleProductsService;
import sk.hfa.projects.domain.Project;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@Service
public class GoogleProductsService implements IGoogleProductsService {

    private static final String CHANNEL = "online";
    private static final String CONTENT_LANGUAGE = "sk";

    private final IGoogleProductsClientService googleProductsClientService;

    public GoogleProductsService(IGoogleProductsClientService googleProductsClientService) {
        this.googleProductsClientService = googleProductsClientService;
    }

    @Override
    public void createGoogleProduct(Project project) {
        Product googleProduct = from(project);
        googleProductsClientService.insertProduct(googleProduct);
        log.info("Google Product for Project with the ID:[" + project.getId() + "] was successfully added/updated.");
    }

    @Override
    public void removeGoogleProduct(String productId) {
        if (StringUtils.isBlank(productId))
            throw new IllegalArgumentException("Google Product ID cannot be blank.");

        googleProductsClientService.deleteProduct(productId);
        log.info("Google Product with the ID:[" + productId + "] was successfully deleted.");
    }

    private Product from(Project project) {
        if (Objects.isNull(project))
            throw new IllegalArgumentException("Input Project content cannot be null.");

        Product googleProduct = new Product();
        setBasicInfo(googleProduct, project);
        setCustomAttributes(googleProduct, project);
        return googleProduct;
    }

    private void setBasicInfo(Product googleProduct, Project project) {
        // TODO:
        googleProduct.setOfferId(String.valueOf(project.getId()));
        googleProduct.setChannel(CHANNEL);
        googleProduct.setContentLanguage(CONTENT_LANGUAGE);
        googleProduct.setTargetCountry(CONTENT_LANGUAGE.toUpperCase(Locale.ROOT));
    }

    private void setCustomAttributes(Product googleProduct, Project project) {
        CustomAttribute attr = new CustomAttribute();
        // TODO:
        // attr.set("Zastavaná plocha", project.getBuiltUpArea());
    }

}
