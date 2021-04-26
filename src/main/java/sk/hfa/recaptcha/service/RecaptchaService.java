package sk.hfa.recaptcha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import sk.hfa.recaptcha.domain.GoogleResponse;
import sk.hfa.recaptcha.domain.throwable.RecaptchaException;

import java.net.URI;

@Service
public class RecaptchaService extends AbstractRecaptchaService {

    @Autowired
    private RecaptchaAttemptService recaptchaAttemptService;

    @Override
    public void processResponse(final String response) {
        securityCheck(response);

        if (recaptchaAttemptService.isBlocked(getClientIP()))
            throw new RecaptchaException("Client is blocked");


        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

            if (googleResponse == null || !googleResponse.isSuccess()) {
                recaptchaAttemptService.reCaptchaFailed(getClientIP());
                throw new RecaptchaException("reCaptcha was not successfully validated");
            }
            recaptchaAttemptService.reCaptchaSucceeded(getClientIP());
        } catch (RestClientException rce) {
            throw new RecaptchaException("Registration unavailable at this time.  Please try again later.");
        }
    }
}
