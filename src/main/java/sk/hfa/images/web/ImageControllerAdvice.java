package sk.hfa.images.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.images.domain.throwable.FetchFileSystemResourceException;
import sk.hfa.images.domain.throwable.ImageNotFoundException;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@ControllerAdvice(assignableTypes = ImageController.class)
public class ImageControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleImageUploadException(ImageNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleFetchFileSystemResourceException(FetchFileSystemResourceException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestResponse(ex.getMessage());
    }

    private ResponseEntity<MessageResource> buildBadRequestResponse(String message) {
        MessageResource responseBody = ErrorMessageResource.build(Constants.BAD_REQUEST_TITLE, message,
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

}
