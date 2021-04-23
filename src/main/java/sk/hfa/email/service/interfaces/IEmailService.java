package sk.hfa.email.service.interfaces;

import sk.hfa.contactform.domain.requestbodies.ContactFormRequest;

public interface IEmailService {

    void send(ContactFormRequest contactFormRequest);

}
