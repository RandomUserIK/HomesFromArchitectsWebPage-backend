package sk.hfa.email.service.utils;

import lombok.Data;
import sk.hfa.form.web.domain.requestbodies.ContactFormRequest;

@Data
public class ContactEmailBuilder {

    private ContactEmailBuilder() {
    }

    public static String build(ContactFormRequest contactFormRequest) {
        String newLine = System.getProperty("line.separator");

        return "Meno: " + contactFormRequest.getName() +
                newLine +
                "Priezvisko: " + contactFormRequest.getSurname() +
                newLine +
                "Telefón: " + contactFormRequest.getTelephone() +
                newLine +
                "Email: " + contactFormRequest.getEmail() +
                newLine +
                newLine +
                "Správa:" +
                newLine +
                contactFormRequest.getText();
    }
}
