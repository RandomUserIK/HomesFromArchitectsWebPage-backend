package sk.hfa.email.service.utils;

import lombok.Data;
import sk.hfa.contact_form.domain.requestbodies.ContactFormRequest;

@Data
public class EmailTextBuilder {

    public static String build(ContactFormRequest contactFormRequest) {
        String newLine = System.getProperty("line.separator");

        return "Name: " + contactFormRequest.getName() +
                newLine +
                "Surname: " + contactFormRequest.getSurname() +
                newLine +
                "Telephone: " + contactFormRequest.getTelephone() +
                newLine +
                "email: " + contactFormRequest.getEmail() +
                newLine +
                newLine +
                contactFormRequest.getText();
    }
}
