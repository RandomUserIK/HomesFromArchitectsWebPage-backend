package sk.hfa.form.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.hfa.email.service.interfaces.IEmailService;
import sk.hfa.form.web.domain.requestbodies.ContactFormRequest;
import sk.hfa.form.web.domain.requestbodies.OrderFormRequest;
import sk.hfa.form.web.domain.responsebodies.SubmittedFormMessageResource;
import sk.hfa.recaptcha.service.interfaces.IRecaptchaService;
import sk.hfa.web.domain.responsebodies.MessageResource;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/form")
public class FormController {

    @Autowired
    private IRecaptchaService recaptchaService;

    @Autowired
    private IEmailService emailService;


    @PostMapping("/order")
    public ResponseEntity<MessageResource> sendOrder(@Valid @RequestBody OrderFormRequest orderForm) {
        recaptchaService.processResponse(orderForm.getRecaptchaToken());
        emailService.send(orderForm);
        MessageResource messageResource = new SubmittedFormMessageResource("success");
        return ResponseEntity.ok(messageResource);
    }

    @PostMapping("/contact")
    public ResponseEntity<MessageResource> sendContact(@Valid @RequestBody ContactFormRequest contactForm) {
        recaptchaService.processResponse(contactForm.getRecaptchaToken());
        emailService.send(contactForm);
        MessageResource responseBody = new SubmittedFormMessageResource("success");
        return ResponseEntity.ok(responseBody);
    }

}
