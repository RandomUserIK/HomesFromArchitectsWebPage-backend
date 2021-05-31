package sk.hfa.blog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.blog.domain.throwable.BlogArticleNotFoundException;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@ControllerAdvice(assignableTypes = BlogController.class)
public class BlogControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build(Constants.BAD_REQUEST_TITLE, ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleInvalidPageableRequestException(InvalidPageableRequestException ex) {
        log.error(ex.getMessage(), ex);
        return buildInternalServerErrorMessageResource(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleBlogArticleNotFoundException(BlogArticleNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return buildInternalServerErrorMessageResource(ex.getMessage());
    }

    private ResponseEntity<MessageResource> buildInternalServerErrorMessageResource(String message) {
        MessageResource responseBody = ErrorMessageResource.build(Constants.INTERNAL_SERVER_ERROR_TITLE, message,
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
