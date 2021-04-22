package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.projects.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.projects.domain.throwable.ImageUploadException;
import sk.hfa.projects.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.io.IOException;

@Slf4j
@ControllerAdvice(assignableTypes = ImageController.class)
public class ImageControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleImageUploadException(ImageUploadException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleFetchFileSystemResourceException(FetchFileSystemResourceException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleIOException(IOException ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build("Internal server error", ex.getMessage(),
                                                                        HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    private ResponseEntity<MessageResource> buildBadRequestResponse(String message) {
        MessageResource responseBody = ErrorMessageResource.build("Bad request", message,
                                                                        HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

}
