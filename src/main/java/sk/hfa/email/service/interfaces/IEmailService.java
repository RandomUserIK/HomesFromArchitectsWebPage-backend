package sk.hfa.email.service.interfaces;

import sk.hfa.contact_form.domain.requestbodies.ContactFormRequest;

public interface IEmailService {

    void send(ContactFormRequest contactFormRequest);

}
