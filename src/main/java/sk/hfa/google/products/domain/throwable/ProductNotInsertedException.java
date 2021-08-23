package sk.hfa.google.products.domain.throwable;

public class ProductNotInsertedException extends RuntimeException {

    public ProductNotInsertedException(String message) {
        super(message);
    }

}
