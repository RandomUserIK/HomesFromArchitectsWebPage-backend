package sk.hfa.form.web.domain.requestbodies;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class ContactFormRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp="^[+]([0-9]){3}( )?([0-9]){3}( )?([0-9]){3}( )?([0-9]){3}$", message="Invalid telephone format")
    private String telephone;

    @NotBlank
    private String text;

    @NotBlank
    private String recaptchaToken;
}
