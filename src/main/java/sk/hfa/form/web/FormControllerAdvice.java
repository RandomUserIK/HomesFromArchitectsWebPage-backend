package sk.hfa.form.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.web.domain.responsebodies.ErrorMessageResource;
import sk.hfa.recaptcha.domain.throwable.RecaptchaException;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@ControllerAdvice(assignableTypes = FormController.class)
public class FormControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleAccountStatusException(RecaptchaException ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build("Bad request", "invalid-recaptcha",
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleAccountStatusException(Exception ex) {
        log.error(ex.getMessage(), ex);
        MessageResource responseBody = ErrorMessageResource.build("Bad request", ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseBody);
    }

}
