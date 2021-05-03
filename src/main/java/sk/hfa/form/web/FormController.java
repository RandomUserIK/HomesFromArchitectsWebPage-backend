package sk.hfa.form.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.email.service.interfaces.IEmailService;
import sk.hfa.form.web.domain.requestbodies.ContactFormRequest;
import sk.hfa.form.web.domain.requestbodies.OrderFormRequest;
import sk.hfa.form.web.domain.responsebodies.SubmittedFormMessageResource;
import sk.hfa.recaptcha.service.interfaces.IRecaptchaService;
import sk.hfa.web.domain.responsebodies.MessageResource;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/api/form")
public class FormController {

    private final IRecaptchaService recaptchaService;

    private final IEmailService emailService;

    public FormController(IRecaptchaService recaptchaService, IEmailService emailService) {
        this.recaptchaService = recaptchaService;
        this.emailService = emailService;
    }


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
