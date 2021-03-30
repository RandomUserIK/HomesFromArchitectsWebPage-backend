package sk.hfa.projects.domain.throwable;

public class InvalidPageableRequestException extends RuntimeException {

    public InvalidPageableRequestException(String s) {
        super(s);
    }

}
