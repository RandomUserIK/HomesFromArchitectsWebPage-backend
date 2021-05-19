package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.InvalidProjectRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@ControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectControllerAdvice {

    private static final String BAD_REQUEST_TITLE = "Bad request";

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleInvalidProjectRequestException(InvalidProjectRequestException ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build(BAD_REQUEST_TITLE, ex.getMessage(),
                                                                    HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build(BAD_REQUEST_TITLE, ex.getMessage(),
                                                                    HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleIllegalArgumentException(EmptyResultDataAccessException ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build(BAD_REQUEST_TITLE, ex.getMessage(),
                                                                    HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleInvalidPageableRequestException(InvalidPageableRequestException ex) {
        log.error(ex.getMessage(), ex);
        return buildInternalServerErrorMessageResource(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleProjectNotFoundException(ProjectNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return buildInternalServerErrorMessageResource(ex.getMessage());
    }

    private ResponseEntity<MessageResource> buildInternalServerErrorMessageResource(String message) {
        MessageResource responseBody = ErrorMessageResource.build("Internal server error", message,
                                                                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
