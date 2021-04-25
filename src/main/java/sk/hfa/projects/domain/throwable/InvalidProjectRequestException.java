package sk.hfa.projects.domain.throwable;

public class InvalidProjectRequestException extends RuntimeException {

    public InvalidProjectRequestException(String message) {
        super(message);
    }

}
