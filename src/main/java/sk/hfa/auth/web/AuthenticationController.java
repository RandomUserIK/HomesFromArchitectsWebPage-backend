package sk.hfa.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.hfa.auth.service.interfaces.IAuthenticationService;
import sk.hfa.auth.web.requestbodies.AuthenticationRequest;
import sk.hfa.auth.web.responsebodies.AuthenticationResponse;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
// TODO: remove after CORS policy is configured
@CrossOrigin("http://localhost:4200")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticateUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

}
