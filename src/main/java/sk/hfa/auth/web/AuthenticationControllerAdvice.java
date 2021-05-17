package sk.hfa.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.hfa.auth.web.responsebodies.AuthenticationErrorResponse;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@ControllerAdvice(assignableTypes = AuthenticationController.class)
public class AuthenticationControllerAdvice {

    private static final String FAILED_TO_AUTHENTICATE_MESSAGE = "Failed to authenticate user.";

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Invalid credentials provided", ex);
        return new ResponseEntity<>(AuthenticationErrorResponse.build(FAILED_TO_AUTHENTICATE_MESSAGE), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleDisabledException(DisabledException ex) {
        log.error("Disabled", ex);
        return new ResponseEntity<>(AuthenticationErrorResponse.build(FAILED_TO_AUTHENTICATE_MESSAGE), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleLockedException(LockedException ex) {
        log.error("Locked", ex);
        return new ResponseEntity<>(AuthenticationErrorResponse.build(FAILED_TO_AUTHENTICATE_MESSAGE), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResource> handleAccountStatusException(AccountStatusException ex) {
        log.error("Invalid user account state", ex);
        return new ResponseEntity<>(AuthenticationErrorResponse.build(FAILED_TO_AUTHENTICATE_MESSAGE), HttpStatus.UNAUTHORIZED);
    }

}
