package sk.hfa.google.products.configuration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.ShoppingContentScopes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import sk.hfa.google.products.domain.MerchantInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@Slf4j
@Configuration
public class GoogleProductsConfiguration {

    @Value("${google.products.service-account}")
    private Resource serviceAccountResource;

    @Value("${google.products.merchant-info}")
    private Resource merchantInfoResource;

    @Bean
    public ShoppingContent googleProductsClient() {
        return new ShoppingContent.Builder(httpTransport(), JacksonFactory.getDefaultInstance(), getCredential())
                .setApplicationName("Content API for HFA Products")
                .build();
    }

    @Bean
    public MerchantInfo merchantInfo() {
        if (Objects.isNull(merchantInfoResource) || !merchantInfoResource.exists()) {
            log.error("merchant-info.json file not found");
            System.exit(-1);
        }

        return getMerchantInfo();
    }

    @Bean
    public HttpTransport httpTransport() {
        try {
            return GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException ex) {
            log.error("Failed to create HTTP Transport", ex);
            System.exit(-1);
        }
        return null;
    }

    private MerchantInfo getMerchantInfo() {
        try {
            return JacksonFactory.getDefaultInstance()
                    .fromInputStream(merchantInfoResource.getInputStream(), MerchantInfo.class);
        } catch (IOException | NullPointerException ex) {
            log.error("Failed to parse merchant-info.json file.", ex);
            System.exit(-1);
        }
        return null;
    }

    private Credential getCredential() {
        try {
            return GoogleCredential
                    .fromStream(serviceAccountResource.getInputStream(), httpTransport(), JacksonFactory.getDefaultInstance())
                    .createScoped(ShoppingContentScopes.all());
        } catch (IOException | NullPointerException ex) {
            log.error("Failed to parse Google Products service-account info.", ex);
            System.exit(-1);
        }
        return null;
    }

}
