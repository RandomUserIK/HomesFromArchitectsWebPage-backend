package sk.hfa.projects.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.projects.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.projects.web.domain.responsebodies.MessageResource;

@ControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleIllegalArgumentException(IllegalArgumentException ex) {
        MessageResource responseBody = ErrorMessageResource.build("Bad request", ex.getMessage(),
                                                                        HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleInvalidPageableRequestException(InvalidPageableRequestException ex) {
        return buildInternalServerErrorMessageResource(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleProjectNotFoundException(ProjectNotFoundException ex) {
        return buildInternalServerErrorMessageResource(ex.getMessage());
    }

    private ResponseEntity<MessageResource> buildInternalServerErrorMessageResource(String message) {
        MessageResource responseBody = ErrorMessageResource.build("Internal server error", message,
                                                                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
