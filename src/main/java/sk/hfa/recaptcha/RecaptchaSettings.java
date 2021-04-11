package sk.hfa.recaptcha;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaSettings {
    private String site;
    private String secret;
}
