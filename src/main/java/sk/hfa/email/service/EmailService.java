package sk.hfa.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sk.hfa.contactform.domain.requestbodies.ContactFormRequest;
import sk.hfa.email.service.interfaces.IEmailService;
import sk.hfa.email.service.utils.EmailTextBuilder;

@Service
public class EmailService implements IEmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailServerUsername;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void send(ContactFormRequest contactFormRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailServerUsername);
        message.setSubject("Kontaktný formulár");
        message.setText((EmailTextBuilder.build(contactFormRequest)));
        emailSender.send(message);
    }

}
