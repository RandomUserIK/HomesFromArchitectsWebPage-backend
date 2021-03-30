package sk.hfa.rest.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.rest.domain.throwable.InvalidPageableRequestException;
import sk.hfa.rest.responsebodies.InvalidPageRequestMessageResource;



@ControllerAdvice(assignableTypes = {ProjectController.class, PhotoController.class})
public class RestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<InvalidPageRequestMessageResource> handleFieldsException(InvalidPageableRequestException ex) {
        InvalidPageRequestMessageResource messageResource = new InvalidPageRequestMessageResource();
        messageResource.setCode(HttpStatus.NOT_FOUND.toString());
        messageResource.setMessage(ex.getMessage());
        messageResource.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(messageResource, HttpStatus.NOT_FOUND);
    }

}
