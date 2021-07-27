package sk.hfa.google.products.services;

import com.google.api.services.content.ShoppingContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sk.hfa.google.products.services.interfaces.IGoogleProductsClientService;

@Slf4j
@Service
public class GoogleProductsClientService implements IGoogleProductsClientService {

    private final ShoppingContent googleProductsClient;

    public GoogleProductsClientService(ShoppingContent googleProductsClient) {
        this.googleProductsClient = googleProductsClient;
    }

}
