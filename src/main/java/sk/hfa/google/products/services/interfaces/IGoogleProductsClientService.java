package sk.hfa.google.products.services.interfaces;

import com.google.api.services.content.model.Product;

public interface IGoogleProductsClientService {

    String insertProduct(Product product);

    void deleteProduct(String productId);

}
