package sk.hfa.contact_form.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.contact_form.domain.requestbodies.ContactFormRequest;
import sk.hfa.contact_form.domain.responsebodies.SubmittedContactFormResponse;
import sk.hfa.email.service.interfaces.IEmailService;
import sk.hfa.projects.web.domain.responsebodies.MessageResource;
import sk.hfa.recaptcha.service.interfaces.IRecaptchaService;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/api/contact")
public class ContactController {

    private final IRecaptchaService recaptchaService;

    private final IEmailService emailService;

    public ContactController(IRecaptchaService recaptchaService, IEmailService emailService) {
        this.recaptchaService = recaptchaService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<MessageResource> getContactForm(@Valid @RequestBody ContactFormRequest contactForm) {
        recaptchaService.processResponse(contactForm.getRecaptchaToken());
        emailService.send(contactForm);
        MessageResource responseBody = new SubmittedContactFormResponse("success");
        return ResponseEntity.ok(responseBody);
    }

}
