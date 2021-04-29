package sk.hfa.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sk.hfa.form.web.domain.requestbodies.ContactFormRequest;
import sk.hfa.email.service.interfaces.IEmailService;
import sk.hfa.email.service.utils.ContactEmailBuilder;
import sk.hfa.email.service.utils.OrderEmailBuilder;
import sk.hfa.form.web.domain.requestbodies.OrderFormRequest;

@Service
public class EmailService implements IEmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailServerUsername;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(OrderFormRequest orderFormRequest) {
        SimpleMailMessage message = initSimpleMailMessage("Objednávkový formulár");
        message.setText(OrderEmailBuilder.build(orderFormRequest));
        emailSender.send(message);
    }

    @Override
    public void send(ContactFormRequest contactFormRequest) {
        SimpleMailMessage message = initSimpleMailMessage("Kontaktný formulár");
        message.setText(ContactEmailBuilder.build(contactFormRequest));
        emailSender.send(message);
    }

    private SimpleMailMessage initSimpleMailMessage(String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailServerUsername);
        message.setSubject(subject);
        return message;
    }

}
