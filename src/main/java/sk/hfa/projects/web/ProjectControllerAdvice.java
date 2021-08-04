package sk.hfa.projects.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.projects.domain.throwable.InvalidProjectRequestException;
import sk.hfa.projects.domain.throwable.ProjectNotFoundException;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@ControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleInvalidProjectRequestException(InvalidProjectRequestException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestMessageResource(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleMethodNotValidArgument(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestMessageResource("Validation failed. Invalid request body.");
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestMessageResource(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error(ex.getMessage(), ex);
        return buildBadRequestMessageResource(ex.getMessage());
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
        MessageResource responseBody = ErrorMessageResource.build(Constants.INTERNAL_SERVER_ERROR_TITLE, message,
                                                                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    private ResponseEntity<MessageResource> buildBadRequestMessageResource(String message) {
        MessageResource responseBody = ErrorMessageResource.build(Constants.BAD_REQUEST_TITLE, message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

}
