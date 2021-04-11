package sk.hfa.contact_form.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.contact_form.domain.requestbodies.ContactFormRequest;
import sk.hfa.contact_form.domain.responsebodies.SubmittedContactFormResponse;
import sk.hfa.projects.web.domain.responsebodies.MessageResource;
import sk.hfa.recaptcha.service.interfaces.IRecaptchaService;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class ContactController {

    private final IRecaptchaService recaptchaService;

    public ContactController(IRecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @PostMapping("/contact")
    public ResponseEntity<MessageResource> getContactForm(@RequestBody ContactFormRequest contactForm) {
        recaptchaService.processResponse(contactForm.getRecaptchaToken());
        MessageResource responseBody = new SubmittedContactFormResponse("success");
        return ResponseEntity.ok(responseBody);
    }

}
