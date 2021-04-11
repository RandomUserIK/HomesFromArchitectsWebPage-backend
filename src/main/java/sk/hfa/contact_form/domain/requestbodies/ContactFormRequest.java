package sk.hfa.contact_form.domain.requestbodies;

import lombok.Data;

@Data
public class ContactFormRequest {
    private String name;
    private String surname;
    private String email;
    private String telephone;
    private String text;
    private String recaptchaToken;
}
