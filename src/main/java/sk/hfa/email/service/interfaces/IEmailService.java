package sk.hfa.email.service.interfaces;

import sk.hfa.form.web.domain.requestbodies.ContactFormRequest;
import sk.hfa.form.web.domain.requestbodies.OrderFormRequest;

public interface IEmailService {

    void send(OrderFormRequest orderFormRequest);

    void send(ContactFormRequest contactFormRequest);

}
