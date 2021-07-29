package sk.hfa.google.products.domain.throwable;

public class ProductNotDeletedException extends RuntimeException {

    public ProductNotDeletedException(String message) {
        super(message);
    }

}
