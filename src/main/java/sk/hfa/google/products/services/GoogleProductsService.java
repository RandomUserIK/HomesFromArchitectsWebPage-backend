package sk.hfa.google.products.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sk.hfa.google.products.services.interfaces.IGoogleProductsClientService;
import sk.hfa.google.products.services.interfaces.IGoogleProductsService;

@Slf4j
@Service
public class GoogleProductsService implements IGoogleProductsService {

    private final IGoogleProductsClientService googleProductsClientService;

    public GoogleProductsService(IGoogleProductsClientService googleProductsClientService) {
        this.googleProductsClientService = googleProductsClientService;
    }

}
