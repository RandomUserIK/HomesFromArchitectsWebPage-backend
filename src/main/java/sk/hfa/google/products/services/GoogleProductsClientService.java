package sk.hfa.google.products.services;

import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sk.hfa.google.products.domain.MerchantInfo;
import sk.hfa.google.products.domain.throwable.ProductNotDeletedException;
import sk.hfa.google.products.domain.throwable.ProductNotInsertedException;
import sk.hfa.google.products.services.interfaces.IGoogleProductsClientService;

import java.io.IOException;

@Slf4j
@Service
public class GoogleProductsClientService implements IGoogleProductsClientService {

    private final ShoppingContent googleProductsClient;

    private final MerchantInfo merchantInfo;

    public GoogleProductsClientService(ShoppingContent googleProductsClient, MerchantInfo merchantInfo) {
        this.googleProductsClient = googleProductsClient;
        this.merchantInfo = merchantInfo;
    }

    @Override
    public String insertProduct(Product product) {
        try {
            return googleProductsClient.products().insert(merchantInfo.getMerchantId(), product).execute().getId();
        } catch (IOException ex) {
            final String message = "Failed to insert/update product.";
            log.error(message, ex);
            throw new ProductNotInsertedException(message);
        }
    }

    @Override
    public void deleteProduct(String productId) {
        try {
            googleProductsClient.products().delete(merchantInfo.getMerchantId(), productId).execute();
        } catch (IOException ex) {
            final String message = "Failed to remove product with the ID: [" + productId + "].";
            log.error(message, ex);
            throw new ProductNotDeletedException(message);
        }
    }

}
