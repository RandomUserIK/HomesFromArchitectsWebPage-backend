package sk.hfa.form.web.domain.requestbodies;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class ContactFormRequest {

    @NotBlank
    @Length(max = 20, message = "Contact name must have number of chars between 0, 20")
    private String name;

    @NotBlank
    @Length(max = 20, message = "Contact surname must have number of chars between 0, 20")
    private String surname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[+]([0-9]){3}( )?([0-9]){3}( )?([0-9]){3}( )?([0-9]){3}$", message = "Invalid telephone format")
    private String telephone;

    @NotBlank
    @Length(max = 450, message = "Contact text must have number of chars between 0, 450")
    private String text;

    @NotBlank
    private String recaptchaToken;
}
