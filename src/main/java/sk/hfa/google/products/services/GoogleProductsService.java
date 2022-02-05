package sk.hfa.google.products.services;

import com.google.api.services.content.model.CustomAttribute;
import com.google.api.services.content.model.Price;
import com.google.api.services.content.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.hfa.google.products.services.interfaces.IGoogleProductsClientService;
import sk.hfa.google.products.services.interfaces.IGoogleProductsService;
import sk.hfa.google.products.util.GoogleProductsFieldMappings;
import sk.hfa.projects.domain.CommonProject;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.enums.Category;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class GoogleProductsService implements IGoogleProductsService {

    private static final String CHANNEL = "online";
    private static final String CONTENT_LANGUAGE = "sk";
    private static final String CURRENCY = "EUR";

    @Value("${google.products.use-content-api}")
    private boolean useContentApi;

    private final IGoogleProductsClientService googleProductsClientService;

    public GoogleProductsService(IGoogleProductsClientService googleProductsClientService) {
        this.googleProductsClientService = googleProductsClientService;
    }

    @Override
    public String createGoogleProduct(Project project) {
        if (!useContentApi)
            return null;
        
        Product googleProduct = from(project);
        String googleProductId = googleProductsClientService.insertProduct(googleProduct);
        log.info("Google Product for Project with the ID:[" + project.getId() + "] was successfully added/updated.");
        return googleProductId;
    }

    @Override
    public void removeGoogleProduct(String googleProductId) {
        if (!useContentApi)
            return;

        if (StringUtils.isBlank(googleProductId))
            throw new IllegalArgumentException("Google Product ID cannot be blank.");

        googleProductsClientService.deleteProduct(googleProductId);
        log.info("Google Product with the ID:[" + googleProductId + "] was successfully deleted.");
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
        googleProduct.setOfferId(String.valueOf(project.getId()));
        googleProduct.setTitle(project.getTitle());
        googleProduct.setChannel(CHANNEL);
        googleProduct.setContentLanguage(CONTENT_LANGUAGE);
        googleProduct.setTargetCountry(CONTENT_LANGUAGE.toUpperCase(Locale.ROOT));
        // TODO how is this field set
        googleProduct.setDescription(project.getDescription());
        // TODO: poriesit identifier
        googleProduct.setIdentifierExists(false);

        if (Category.COMMON.equals(project.getCategory()))
            googleProduct.setPrice(
                    new Price()
                            .setValue(String.valueOf(((CommonProject) project).getBasicProjectPrice()))
                            .setCurrency(CURRENCY)
            );
    }

    private void setCustomAttributes(Product googleProduct, Project project) {
        if (Objects.isNull(googleProduct.getCustomAttributes()))
            googleProduct.setCustomAttributes(new ArrayList<>());

        setAttributeValues(googleProduct, project, GoogleProductsFieldMappings.DEFAULT_PROJECT_FIELD_MAPPINGS, true);

        if (Category.COMMON.equals(project.getCategory()))
            setAttributeValues(googleProduct, project, GoogleProductsFieldMappings.COMMON_PROJECT_FIELD_MAPPINGS, false);
    }

    private void setAttributeValues(Product googleProduct, Project project, Map<String, String> fieldMappings, boolean isDefault) {
        fieldMappings.forEach((key, value) -> {
            try {
                CustomAttribute customAttribute = getCustomAttribute(project, key, value, isDefault);
                if (!Objects.isNull(customAttribute))
                    googleProduct.getCustomAttributes().add(customAttribute);
            } catch (NoSuchFieldException | NullPointerException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                log.warn("Failed to set custom attribute value. --> " + ex.getMessage());
            }
        });
    }

    private CustomAttribute getCustomAttribute(Project project, String fieldId, String attributeName, boolean isDefault) throws NoSuchFieldException, IllegalAccessException {
        Class<? extends Project> projectClass = project.getClass();
        Field projectField = isDefault ? projectClass.getSuperclass().getDeclaredField(fieldId) : projectClass.getDeclaredField(fieldId);
        projectField.setAccessible(true); // NOSONAR
        Object fieldValue = projectField.get(project);

        if (Objects.isNull(fieldValue))
            return null;

        CustomAttribute customAttribute = new CustomAttribute();
        customAttribute.setName(attributeName);
        customAttribute.setValue(String.valueOf(fieldValue));
        return customAttribute;
    }

}
