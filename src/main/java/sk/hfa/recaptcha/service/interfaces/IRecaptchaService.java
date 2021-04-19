package sk.hfa.recaptcha.service.interfaces;

import sk.hfa.recaptcha.domain.throwable.RecaptchaException;

public interface IRecaptchaService {

    void processResponse(final String response) throws RecaptchaException;

    String getReCaptchaSecret();
}
