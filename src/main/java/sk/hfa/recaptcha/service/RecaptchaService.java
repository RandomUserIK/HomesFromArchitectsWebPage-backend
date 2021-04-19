package sk.hfa.recaptcha.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import sk.hfa.recaptcha.domain.GoogleResponse;
import sk.hfa.recaptcha.domain.throwable.RecaptchaException;

import java.net.URI;

@Service
public class RecaptchaService extends AbstractRecaptchaService {

    @Override
    public void processResponse(final String response) {
        securityCheck(response);

        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

            if (googleResponse == null || !googleResponse.isSuccess()) {
                throw new RecaptchaException("reCaptcha was not successfully validated");
            }
        } catch (RestClientException rce) {
            throw new RecaptchaException("Registration unavailable at this time.  Please try again later.");
        }
    }
}
