package sk.hfa.configuration.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hfa.security.jwt")
public class JwtProperties {
    // TODO
}
